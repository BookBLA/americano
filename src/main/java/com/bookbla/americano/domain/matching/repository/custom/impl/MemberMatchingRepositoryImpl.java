package com.bookbla.americano.domain.matching.repository.custom.impl;

import com.bookbla.americano.domain.matching.repository.custom.MemberMatchingRepositoryCustom;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.repository.entity.QMatchExcludedInfo;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bookbla.americano.domain.matching.repository.entity.QMatchIgnoredInfo.matchIgnoredInfo;
import static com.bookbla.americano.domain.member.repository.entity.QMember.member;
import static com.bookbla.americano.domain.member.repository.entity.QMemberBook.memberBook;

@Repository
@RequiredArgsConstructor
public class MemberMatchingRepositoryImpl implements MemberMatchingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> getMatchingMemberIds(MemberRecommendationDto recommendationDto) {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusDays(14);
        QMatchExcludedInfo matchExcludedInfo = QMatchExcludedInfo.matchExcludedInfo;

        return queryFactory
                .selectDistinct(member.id)
                .from(member)
                .leftJoin(matchExcludedInfo).on(member.id.eq(matchExcludedInfo.excludedMemberId))
                .where(
                        matchExcludedInfo.memberId.ne(recommendationDto.getMemberId()),
                        member.memberStatus.ne(MemberStatus.DELETED),
                        member.memberStatus.ne(MemberStatus.MATCHING_DISABLED),
                        member.memberStatus.ne(MemberStatus.REPORTED),
                        member.memberProfile.gender.ne(Gender.valueOf(recommendationDto.getMemberGender())),
                        member.lastUsedAt.coalesce(LocalDate.parse("1900-01-01").atStartOfDay()).after(twoWeeksAgo)
                )
                .fetch();
    }

    @Override
    public List<MatchedInfo> getAllMatching(List<Long> matchingMemberIds, MemberRecommendationDto recommendationDto) {
        return queryFactory
                .select(member.id, memberBook.id)
                .from(member)
                .innerJoin(memberBook).on(member.id.eq(memberBook.member.id))
                .where(member.id.in(matchingMemberIds),
                        memberBook.isDeleted.isFalse())
                .fetch()
                .stream()
                .map(tuple -> MatchedInfo.from(recommendationDto.getMemberId(), tuple.get(member.id), tuple.get(memberBook.id), recommendationDto.getMemberMatching()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchedInfo> getMatchingInfo(List<Long> matchingMemberIds, MemberRecommendationDto recommendationDto) {
        // 모든 매칭 정보 ( member_id + member_book_id )
        List<MatchedInfo> matches = queryFactory
                .select(member.id, memberBook.id)
                .from(member)
                .innerJoin(memberBook).on(member.id.eq(memberBook.member.id))
                .where(member.id.in(matchingMemberIds),
                        memberBook.isDeleted.isFalse())
                .fetch()
                .stream()
                .map(tuple -> MatchedInfo.from(recommendationDto.getMemberId(), tuple.get(member.id), tuple.get(memberBook.id), recommendationDto.getMemberMatching()))
                .collect(Collectors.toList());

        // 무시된 정보
        List<MatchedInfo> filteredMatches = queryFactory
                .select(member.id, memberBook.id)
                .from(member)
                .innerJoin(memberBook).on(member.id.eq(memberBook.member.id))
                .leftJoin(matchIgnoredInfo).on(member.id.eq(matchIgnoredInfo.ignoredMemberId)
                        .and(memberBook.id.eq(matchIgnoredInfo.ignoredMemberBookId)))
                .where(matchIgnoredInfo.memberId.eq(recommendationDto.getMemberId()))
                .fetch()
                .stream()
                .map(tuple -> MatchedInfo.from(recommendationDto.getMemberId(), tuple.get(member.id), tuple.get(memberBook.id), recommendationDto.getMemberMatching()))
                .collect(Collectors.toList());

        // 모든 매칭 정보 - 무시된 정보
        return matches.stream()
                .filter(match -> filteredMatches.stream()
                        .noneMatch(filteredMatch -> filteredMatch.getMatchedMemberId().equals(match.getMatchedMemberId())))
                .collect(Collectors.toList());
    }

}
