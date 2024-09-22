package com.bookbla.americano.domain.matching.repository.custom.impl;

import com.bookbla.americano.domain.matching.repository.custom.MemberMatchingRepositoryCustom;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bookbla.americano.domain.member.repository.entity.QMember.member;
import static com.bookbla.americano.domain.member.repository.entity.QMemberBook.memberBook;

@Repository
@RequiredArgsConstructor
public class MemberMatchingRepositoryImpl implements MemberMatchingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MatchedInfo> getMatchingMembers(MemberRecommendationDto recommendationDto) {
        Set<Long> excludeMemberIds = recommendationDto.getExcludeMemberIds();

        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusDays(14);

        return queryFactory
                .selectDistinct(member.id, memberBook.id)
                .from(memberBook)
                .join(member).on(memberBook.member.eq(member))
                .where(
                        member.id.ne(recommendationDto.getMemberId()),
                        member.memberStatus.ne(MemberStatus.DELETED),
                        member.memberProfile.gender.ne(Gender.valueOf(recommendationDto.getMemberGender())),
                        member.lastUsedAt.coalesce(LocalDate.parse("1900-01-01").atStartOfDay()).after(twoWeeksAgo),
                        member.id.notIn(excludeMemberIds)
                ).fetch()
                .stream()
                .map(tuple -> MatchedInfo.builder()
                        .matchedMemberId(tuple.get(member.id))
                        .matchedMemberBookId(tuple.get(memberBook.id))
                        .build())
                .collect(Collectors.toList());
    }
}
