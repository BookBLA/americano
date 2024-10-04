package com.bookbla.americano.domain.matching.repository.custom.impl;

import com.bookbla.americano.domain.matching.repository.custom.MemberMatchingRepositoryCustom;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.repository.entity.QMatchExcludedInfo;
import com.bookbla.americano.domain.matching.repository.entity.QMatchIgnoredInfo;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bookbla.americano.domain.matching.repository.entity.QMatchedInfo.matchedInfo;
import static com.bookbla.americano.domain.member.repository.entity.QMember.member;
import static com.bookbla.americano.domain.member.repository.entity.QMemberBook.memberBook;

@Repository
@RequiredArgsConstructor
public class
MemberMatchingRepositoryImpl implements MemberMatchingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MatchedInfo> getMatchingMembers(MemberRecommendationDto recommendationDto) {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusDays(14);
        QMatchExcludedInfo matchExcludedInfo = QMatchExcludedInfo.matchExcludedInfo;
        QMatchIgnoredInfo matchIgnoredInfo = QMatchIgnoredInfo.matchIgnoredInfo;

        // 서브쿼리 : match_ignored_info 에서 앱사용자가 무시한 회원과 그 회원의 책 정보 추출
        JPAQuery<Tuple> subQuery = queryFactory
                .select(member.id, memberBook.id)
                .from(member)
                .innerJoin(memberBook).on(member.id.eq(memberBook.member.id))
                .leftJoin(matchIgnoredInfo).on(member.id.eq(matchIgnoredInfo.ignoredMemberId)
                        .and(memberBook.id.eq(matchIgnoredInfo.ignoredMemberBookId)))
                .where(matchIgnoredInfo.memberId.eq(recommendationDto.getMemberId()));

        return queryFactory
                .selectDistinct(member.id, memberBook.id)
                .from(member)
                .innerJoin(memberBook).on(member.id.eq(memberBook.member.id))
                .leftJoin(matchExcludedInfo).on(member.id.eq(matchExcludedInfo.excludedMemberId))
                .leftJoin(matchIgnoredInfo).on(member.id.eq(matchIgnoredInfo.ignoredMemberId)
                        .and(memberBook.id.eq(matchIgnoredInfo.ignoredMemberBookId)))
                .where(
                        matchExcludedInfo.memberId.ne(recommendationDto.getMemberId()),
                        memberBook.isDeleted.isFalse(),
                        member.memberStatus.ne(MemberStatus.DELETED),
                        member.memberStatus.ne(MemberStatus.MATCHING_DISABLED),
                        member.memberStatus.ne(MemberStatus.REPORTED),
                        member.memberProfile.gender.ne(Gender.valueOf(recommendationDto.getMemberGender())),
                        member.lastUsedAt.coalesce(LocalDate.parse("1900-01-01").atStartOfDay()).after(twoWeeksAgo),
                        // 서브쿼리의 결과 조합 제외
                        JPAExpressions.selectOne()
                                .from(member)
                                .innerJoin(memberBook).on(member.id.eq(memberBook.member.id))
                                .where(
                                        member.id.eq(subQuery.select(member.id)),
                                        memberBook.id.eq(subQuery.select(memberBook.id))
                                )
                                .notExists()
                )
                .fetch()
                .stream()
                .map(tuple -> MatchedInfo.from(recommendationDto.getMemberId(), tuple.get(member.id), tuple.get(memberBook.id), recommendationDto.getMemberMatching()))
                .collect(Collectors.toList());
    }
}
