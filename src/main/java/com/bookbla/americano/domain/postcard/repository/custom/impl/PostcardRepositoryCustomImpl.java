package com.bookbla.americano.domain.postcard.repository.custom.impl;

import com.bookbla.americano.domain.member.repository.entity.QMember;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.custom.PostcardRepositoryCustom;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import com.bookbla.americano.domain.postcard.repository.entity.QPostcard;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardFromResponse;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardToResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bookbla.americano.domain.school.repository.entity.QSchool.school;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostcardRepositoryCustomImpl implements PostcardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostcardFromResponse> getPostcardsFromMember(Long memberId) {
        QMember member = QMember.member;
        QPostcard postcard = QPostcard.postcard;

        return queryFactory.select(Projections.fields(PostcardFromResponse.class
                        , postcard.receiveMember.id.as("memberId")
                        , postcard.receiveMember.memberProfile.name.as("memberName")
                        , postcard.receiveMember.memberProfile.birthDate.as("memberBirthDate")
                        , postcard.receiveMember.memberProfile.gender.as("memberGender")
                        , member.school.name.as("memberSchoolName")
                        , member.memberStyle.profileImageType.imageUrl.as("memberProfileImageUrl")
                        , postcard.id.as("postcardId")
                        , postcard.postcardStatus.as("postcardStatus"))
                )
                .from(member)
                .innerJoin(postcard).on(member.eq(postcard.receiveMember))
                .where(postcard.sendMember.id.eq(memberId))
                .orderBy(postcard.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Long> findReceiveByIdsWithPostcardStatus(Long sendMemberId, PostcardStatus postcardStatus) {
        QPostcard postcard = QPostcard.postcard;
        return queryFactory
                .select(postcard.receiveMember.id)
                .from(postcard)
                .where(
                        postcard.sendMember.id.eq(sendMemberId),
                        postcard.postcardStatus.eq(postcardStatus)
                );
    }

    public List<PostcardToResponse> getPostcardsToMember(Long memberId) {
        QMember member = QMember.member;
        QPostcard postcard = QPostcard.postcard;

        return queryFactory.select(Projections.fields(PostcardToResponse.class
                        , postcard.id.as("postcardId")
                        , postcard.sendMember.id.as("memberId")
                        , postcard.sendMember.memberProfile.name.as("memberName")
                        , postcard.sendMember.memberProfile.birthDate.as("memberBirthDate")
                        , postcard.sendMember.memberProfile.gender.as("memberGender")
                        , member.school.name.as("memberSchoolName")
                        , member.memberStyle.profileImageType.imageUrl.as("memberProfileImageUrl")
                        , postcard.sendMember.memberStyle.smokeType
                        , postcard.sendMember.memberStyle.mbti
                        , postcard.createdAt.as("receivedTime")
                        , postcard.postcardStatus
                        , postcard.postcardType.imageUrl.as("postcardImageUrl"))
                )
                .from(member)
                .innerJoin(postcard).on(member.eq(postcard.sendMember))
                .where(postcard.receiveMember.id.eq(memberId)
                        .and(postcard.postcardStatus.eq(PostcardStatus.PENDING)
                                .or(postcard.postcardStatus.eq(PostcardStatus.READ))
                                .or(postcard.postcardStatus.eq(PostcardStatus.ACCEPT))))
                .orderBy(postcard.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Long> findReceiveByIdsRefused(Long sendMemberId, List<Postcard> postcards) {
        QPostcard postcard = QPostcard.postcard;
        List<Long> recommendMemberId = postcards.stream()
                .map(pc -> {
                    LocalDateTime fourteenDaysLaterRefuse = pc.getPostcardStatusRefusedAt().plusDays(14);
                    // fourteenDaysLaterRefuse가 now보다 이전인 경우 true를 반환
                    return fourteenDaysLaterRefuse.isBefore(LocalDateTime.now()) ? pc.getReceiveMember().getId() : null;
                })
                .filter(id -> id != null)
                .collect(Collectors.toList());

        return queryFactory
                .select(postcard.receiveMember.id)
                .from(postcard)
                .where(
                        postcard.sendMember.id.eq(sendMemberId),
                        postcard.postcardStatus.eq(PostcardStatus.REFUSED),
                        postcard.receiveMember.id.in(recommendMemberId)
                );
    }

    public List<Postcard> refuseExpiredPostcard() {
        QPostcard postcard = QPostcard.postcard;

        LocalDate now = LocalDate.now();
        LocalDateTime expireDate = now.minusDays(2).atStartOfDay();

        return queryFactory.selectFrom(postcard)
                .where(postcard.createdAt.before(expireDate)
                        .and(postcard.postcardStatus.eq(PostcardStatus.PENDING)
                                .or(postcard.postcardStatus.eq(PostcardStatus.READ))))
                .fetch();
    }
}
