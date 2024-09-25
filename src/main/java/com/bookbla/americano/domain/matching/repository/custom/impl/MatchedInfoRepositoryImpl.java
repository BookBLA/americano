package com.bookbla.americano.domain.matching.repository.custom.impl;

import com.bookbla.americano.domain.matching.repository.MatchedInfoRepository;
import com.bookbla.americano.domain.matching.repository.custom.MatchedInfoRepositoryCustom;
import com.bookbla.americano.domain.matching.repository.entity.MatchedInfo;
import com.bookbla.americano.domain.matching.repository.entity.MemberMatching;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.bookbla.americano.domain.matching.repository.entity.QMatchExcludedInfo.matchExcludedInfo;
import static com.bookbla.americano.domain.matching.repository.entity.QMatchIgnoredInfo.matchIgnoredInfo;
import static com.bookbla.americano.domain.matching.repository.entity.QMatchedInfo.matchedInfo;
import static com.bookbla.americano.domain.member.repository.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MatchedInfoRepositoryImpl implements MatchedInfoRepositoryCustom {

    private final MatchedInfoRepository matchedInfoRepository;

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public List<MatchedInfo> findAllByMemberMatchingId(Long memberMatchingId) {
        return queryFactory
                .selectFrom(matchedInfo)
                .innerJoin(member).on(matchedInfo.matchedMemberId.eq(member.id))
                .leftJoin(matchExcludedInfo).on(matchedInfo.matchedMemberId.eq(matchExcludedInfo.excludedMemberId))
                .leftJoin(matchIgnoredInfo).on(matchedInfo.matchedMemberId.eq(matchIgnoredInfo.ignoredMemberId))
                .where(
                        matchedInfo.memberMatching.id.eq(memberMatchingId),
                        matchExcludedInfo.excludedMemberId.isNull(),
                        matchIgnoredInfo.ignoredMemberId.isNull(),
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

    @Override
    @Transactional
    public void saveAllRecommendedMembers(List<MatchedInfo> recommendedMembers) {
        int batchSize = 50;

        for (int i = 0; i < recommendedMembers.size(); i++) {
            matchedInfoRepository.save(recommendedMembers.get(i));

            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }

        em.flush();
        em.clear();
    }

    @Override
    @Transactional
    public void updateAllRecommendedMembers(MemberMatching memberMatching, List<MatchedInfo> recommendedMembers) {
        int batchSize = 50;

        for (int i = 0; i < recommendedMembers.size(); i++) {
            memberMatching.updateMatched(recommendedMembers.get(i));

            if (i > 0 && i % batchSize == 0) {
                em.flush();
                em.clear();
            }
        }

        em.flush();
        em.clear();
    }
}
