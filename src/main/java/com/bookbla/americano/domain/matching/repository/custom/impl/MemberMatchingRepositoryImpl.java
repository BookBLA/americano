package com.bookbla.americano.domain.matching.repository.custom.impl;

import com.bookbla.americano.domain.matching.repository.custom.MemberMatchingRepositoryCustom;
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

import static com.bookbla.americano.domain.member.repository.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberMatchingRepositoryImpl implements MemberMatchingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> getMinimumConstraintMemberIds(MemberRecommendationDto recommendationDto) {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusDays(14);
        QMatchExcludedInfo matchExcludedInfo = QMatchExcludedInfo.matchExcludedInfo;

        return queryFactory
                .selectDistinct(member.id)
                .from(member)
                .leftJoin(matchExcludedInfo).on(member.id.eq(matchExcludedInfo.excludedMemberId))
                .where(
                        (matchExcludedInfo.memberId.ne(recommendationDto.getMemberId())
                                .or(matchExcludedInfo.excludedMemberId.isNull())),
                        member.memberStatus.ne(MemberStatus.DELETED),
                        member.memberStatus.ne(MemberStatus.MATCHING_DISABLED),
                        member.memberStatus.ne(MemberStatus.REPORTED),
                        member.memberProfile.gender.ne(Gender.valueOf(recommendationDto.getMemberGender())),
                        member.lastUsedAt.coalesce(LocalDate.parse("1900-01-01").atStartOfDay()).after(twoWeeksAgo)
                )
                .fetch();
    }

}
