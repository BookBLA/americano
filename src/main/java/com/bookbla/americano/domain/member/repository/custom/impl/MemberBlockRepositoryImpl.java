package com.bookbla.americano.domain.member.repository.custom.impl;

import com.bookbla.americano.domain.member.repository.custom.MemberBlockRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import static com.bookbla.americano.domain.member.repository.entity.QMemberBlock.memberBlock;

@Repository
@RequiredArgsConstructor
public class MemberBlockRepositoryImpl implements MemberBlockRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> getBlockedMemberIdsByBlockerMemberId(Long memberId, Set<Long> matchingMembers) {

        return queryFactory
                .select(memberBlock.blockedMember.id)
                .from(memberBlock)
                .where(memberBlock.blockerMember.id.eq(memberId),
                        memberBlock.blockedMember.id.in(matchingMembers)
                ).fetch();
    }
}
