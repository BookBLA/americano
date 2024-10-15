package com.bookbla.americano.domain.matching.repository.custom.impl;

import com.bookbla.americano.domain.matching.repository.custom.MatchExcludedRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bookbla.americano.domain.matching.repository.entity.QMatchExcludedInfo.matchExcludedInfo;

@Repository
@RequiredArgsConstructor
public class MatchExcludedRepositoryImpl implements MatchExcludedRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> getExcludedMemberIdsByMemberId(Long memberId, List<Long> matchingMemberIds) {
        return queryFactory
                .select(matchExcludedInfo.excludedMemberId)
                .from(matchExcludedInfo)
                .where(matchExcludedInfo.memberId.eq(memberId),
                        matchExcludedInfo.excludedMemberId.in(matchingMemberIds))
                .fetch();
    }


}
