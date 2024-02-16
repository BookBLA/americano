package com.bookbla.americano.domain.member.repository.impl;

import com.bookbla.americano.domain.book.QBook;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponseDto;
import com.bookbla.americano.domain.member.repository.MemberProfileRepository;
import com.bookbla.americano.domain.member.repository.entity.QMemberBook;
import com.bookbla.americano.domain.member.repository.entity.QMemberProfile;
import com.bookbla.americano.domain.member.repository.entity.QMemberStyle;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class MemberProfileRepositoryImpl implements MemberProfileRepository {
    private final JPAQueryFactory queryFactory;

    public MemberProfileRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<MemberBookProfileResponseDto> searchSameBookMember(MemberBookProfileRequestDto requestDto) {
        QMemberProfile memberProfile = QMemberProfile.memberProfile;
        QMemberStyle memberStyle = QMemberStyle.memberStyle;
        QMemberBook memberBook = QMemberBook.memberBook;
        QBook book = QBook.book;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(memberBook.book.id.in(
                JPAExpressions
                        .select(memberBook.book.id)
                        .from(memberProfile)
                        .innerJoin(memberBook).on(memberProfile.member.eq(memberBook.member))
                        .where(memberProfile.member.id.eq(requestDto.getMemberId()))));
        if (requestDto.getContactType() != null)
            builder.and(memberStyle.contactType.eq(requestDto.getContactType()));
        if (requestDto.getDateCostType() != null)
            builder.and(memberStyle.dateCostType.eq(requestDto.getDateCostType()));
        if (requestDto.getSmokeType() != null)
            builder.and(memberStyle.smokeType.eq(requestDto.getSmokeType()));
        if (requestDto.getDrinkType() != null)
            builder.and(memberStyle.drinkType.eq(requestDto.getDrinkType()));
        if (requestDto.getJustFriendType() != null)
            builder.and(memberStyle.justFriendType.eq(requestDto.getJustFriendType()));
        if (requestDto.getMbti() != null)
            builder.and(memberStyle.mbti.eq(requestDto.getMbti()));

        return queryFactory
                .select(Projections.fields(MemberBookProfileResponseDto.class
                        , memberProfile.member.id.as("memberId")
                        , memberBook.book.id.as("bookId")
                        , memberProfile.nickname.as("memberName")
                        , memberProfile.age.as("memberAge")
                        , memberProfile.gender.as("memberGender")
                        , memberProfile.schoolName.as("memberSchoolName")
                        , book.name.as("bookName")
                        , book.imageUrl.as("bookImageUrl")
                        , memberBook.isRepresentative.as("bookIsRepresentative"))
                )
                .from(memberProfile)
                .innerJoin(memberBook).on(memberProfile.member.eq(memberBook.member))
                .innerJoin(book).on(memberBook.book.eq(book))
                .innerJoin(memberStyle).on(memberProfile.member.eq(memberStyle.member))
                .where(builder.or(memberProfile.member.id.eq(requestDto.getMemberId())))
                .fetch();
    }

}
