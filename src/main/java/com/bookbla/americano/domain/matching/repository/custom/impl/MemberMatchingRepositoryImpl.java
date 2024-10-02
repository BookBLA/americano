package com.bookbla.americano.domain.matching.repository.custom.impl;

import com.bookbla.americano.domain.matching.repository.custom.MemberMatchingRepositoryCustom;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.repository.entity.QMatchExcludedInfo;
import com.bookbla.americano.domain.matching.repository.entity.QMatchIgnoredInfo;
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

import static com.bookbla.americano.domain.matching.repository.entity.QMatchedInfo.matchedInfo;
import static com.bookbla.americano.domain.member.repository.entity.QMember.member;
import static com.bookbla.americano.domain.member.repository.entity.QMemberBook.memberBook;

@Repository
@RequiredArgsConstructor
public class MemberMatchingRepositoryImpl implements MemberMatchingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MatchedInfo> getMatchingMembers(MemberRecommendationDto recommendationDto) {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusDays(14);
        QMatchExcludedInfo matchExcludedInfo = QMatchExcludedInfo.matchExcludedInfo;
        QMatchIgnoredInfo matchIgnoredInfo = QMatchIgnoredInfo.matchIgnoredInfo;

        return queryFactory
                .selectDistinct(member.id, memberBook.id)
                .from(member)
                .innerJoin(memberBook).on(member.id.eq(memberBook.member.id))
                .leftJoin(matchExcludedInfo).on(member.id.eq(matchExcludedInfo.excludedMemberId))
                .leftJoin(matchIgnoredInfo).on(member.id.eq(matchIgnoredInfo.ignoredMemberId))
                .where(
                        matchExcludedInfo.excludedMemberId.ne(recommendationDto.getMemberId()),
                        matchIgnoredInfo.ignoredMemberId.ne(recommendationDto.getMemberId()),
                        member.id.ne(recommendationDto.getMemberId()),
                        memberBook.isDeleted.isFalse(),
                        member.memberStatus.ne(MemberStatus.DELETED),
                        member.memberStatus.ne(MemberStatus.MATCHING_DISABLED),
                        member.memberStatus.ne(MemberStatus.REPORTED),
                        member.memberProfile.gender.ne(Gender.valueOf(recommendationDto.getMemberGender())),
                        member.lastUsedAt.coalesce(LocalDate.parse("1900-01-01").atStartOfDay()).after(twoWeeksAgo))
                .fetch()
                .stream()
                .map(tuple -> MatchedInfo.from(recommendationDto.getMemberId(), tuple.get(member.id), tuple.get(memberBook.id), recommendationDto.getMemberMatching()))
                .collect(Collectors.toList());
    }
}
