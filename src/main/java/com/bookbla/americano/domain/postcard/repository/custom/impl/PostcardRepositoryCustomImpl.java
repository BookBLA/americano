package com.bookbla.americano.domain.postcard.repository.custom.impl;

import com.bookbla.americano.domain.member.repository.entity.QMember;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.custom.PostcardRepositoryCustom;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import com.bookbla.americano.domain.postcard.repository.entity.QPostcard;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardFromResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.bookbla.americano.domain.school.repository.entity.QSchool.school;

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
                        , postcard.receiveMember.memberStyle.profileImageType.imageUrl.as("memberProfileImageUrl")
                        , postcard.id.as("postcardId")
                        , postcard.postcardStatus.as("postcardStatus"))
                )
                .from(postcard)
                .innerJoin(member).on(postcard.receiveMember.eq(member))
                .innerJoin(school).on(member.school.eq(school))
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
                )
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
                )
                .fetch();
    }
}
