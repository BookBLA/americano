package com.bookbla.americano.domain.postcard.repository.custom.impl;

import java.util.List;

import com.bookbla.americano.domain.member.repository.entity.QMember;
import com.bookbla.americano.domain.postcard.repository.custom.PostcardRepositoryCustom;
import com.bookbla.americano.domain.postcard.repository.entity.QPostcard;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardFromResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
