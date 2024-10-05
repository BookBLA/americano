package com.bookbla.americano.domain.matching.repository.custom.impl;

import com.bookbla.americano.domain.matching.repository.custom.MatchIgnoredRepositoryCustom;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.service.dto.MemberRecommendationDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.bookbla.americano.domain.matching.repository.entity.QMatchIgnoredInfo.matchIgnoredInfo;
import static com.bookbla.americano.domain.member.repository.entity.QMember.member;
import static com.bookbla.americano.domain.member.repository.entity.QMemberBook.memberBook;

@Repository
@RequiredArgsConstructor
public class MatchIgnoredRepositoryImpl implements MatchIgnoredRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MatchedInfo> getIgnoredMemberIdsAndIgnoredMemberBookIdByMemberId(List<Long> matchingMemberIds, MemberRecommendationDto memberRecommendationDto) {
        return queryFactory
                .select(member.id, memberBook.id)
                .from(member)
                .innerJoin(memberBook).on(member.id.eq(memberBook.member.id))
                .leftJoin(matchIgnoredInfo).on(member.id.eq(matchIgnoredInfo.ignoredMemberId)
                        .and(memberBook.id.eq(matchIgnoredInfo.ignoredMemberBookId)))
                .where(matchIgnoredInfo.memberId.eq(memberRecommendationDto.getMemberId()))
                .fetch()
                .stream()
                .map(tuple -> MatchedInfo.from(memberRecommendationDto.getMemberId(), tuple.get(member.id), tuple.get(memberBook.id), memberRecommendationDto.getMemberMatching()))
                .collect(Collectors.toList());
    }

}