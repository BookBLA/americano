package com.bookbla.americano.domain.member.repository.custom.impl;

import com.bookbla.americano.domain.member.enums.MemberVerifyStatus;
import com.bookbla.americano.domain.member.enums.MemberVerifyType;
import com.bookbla.americano.domain.member.repository.custom.MemberVerifyRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.bookbla.americano.domain.member.repository.entity.QMember.member;
import static com.bookbla.americano.domain.member.repository.entity.QMemberVerify.memberVerify;

@Repository
@RequiredArgsConstructor
public class MemberVerifyRepositoryCustomImpl implements MemberVerifyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> getMemberIdsByStudentIdVerify(Set<Long> filteringMemberId) {

        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);

        return queryFactory
                .select(memberVerify.memberId)
                .from(memberVerify)
                .join(member).on(memberVerify.memberId.eq(member.id))
                .where(
                        memberVerify.memberId.in(filteringMemberId)
                        .and(
                                (
                                        member.createdAt.before(twoDaysAgo)
                                        .and(memberVerify.verifyType.eq(MemberVerifyType.STUDENT_ID))
                                        .and(memberVerify.verifyStatus.eq(MemberVerifyStatus.SUCCESS))
                                )
                                .or(
                                        (
                                                member.createdAt.goe(twoDaysAgo)
                                                .and(memberVerify.verifyType.eq(MemberVerifyType.STUDENT_ID))
                                        )
                                )
                        )
                ).fetch();
    }

}