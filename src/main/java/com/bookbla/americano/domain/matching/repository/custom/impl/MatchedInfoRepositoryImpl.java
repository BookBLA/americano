package com.bookbla.americano.domain.matching.repository.custom.impl;

import com.bookbla.americano.domain.matching.repository.custom.MatchedInfoRepositoryCustom;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bookbla.americano.domain.matching.repository.entity.QMatchExcludedInfo.matchExcludedInfo;
import static com.bookbla.americano.domain.matching.repository.entity.QMatchIgnoredInfo.matchIgnoredInfo;
import static com.bookbla.americano.domain.matching.repository.entity.QMatchedInfo.matchedInfo;
import static com.bookbla.americano.domain.member.repository.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MatchedInfoRepositoryImpl implements MatchedInfoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MatchedInfo> findAllByMemberMatchingId(Long memberMatchingId) {
        return queryFactory
                .selectFrom(matchedInfo)
                .innerJoin(member).on(matchedInfo.matchedMemberId.eq(member.id))
                .innerJoin(matchExcludedInfo).on(matchedInfo.matchedMemberId.ne(matchExcludedInfo.excludedMemberId))
                .innerJoin(matchIgnoredInfo).on(matchedInfo.matchedMemberId.ne(matchIgnoredInfo.ignoredMemberId))
                .where(
                        matchedInfo.memberMatching.id.eq(memberMatchingId),
                        member.memberStatus.ne(MemberStatus.DELETED),
                        member.memberStatus.ne(MemberStatus.BLOCKED),
                        member.memberStatus.ne(MemberStatus.MATCHING_DISABLED),
                        member.memberStatus.ne(MemberStatus.REPORTED))
                .orderBy(matchedInfo.similarityWeight.desc())
                .fetch();
    }

    @Override
    public List<MatchedInfo> getAllByDesc(Long memberMatchingId) {
        return queryFactory
                .selectFrom(matchedInfo)
                .where(matchedInfo.memberMatching.id.eq(memberMatchingId))
                .orderBy(matchedInfo.similarityWeight.desc())
                .fetch();
    }
}
