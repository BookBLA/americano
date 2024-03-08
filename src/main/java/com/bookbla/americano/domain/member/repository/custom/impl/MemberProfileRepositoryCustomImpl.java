package com.bookbla.americano.domain.member.repository.custom.impl;

import com.bookbla.americano.domain.book.QBook;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponseDto;
import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DateStyleType;
import com.bookbla.americano.domain.member.enums.DrinkType;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.JustFriendType;
import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.SmokeType;
import com.bookbla.americano.domain.member.repository.custom.MemberProfileRepositoryCustom;
import com.bookbla.americano.domain.member.repository.entity.QMemberBook;
import com.bookbla.americano.domain.member.repository.entity.QMemberProfile;
import com.bookbla.americano.domain.member.repository.entity.QMemberStyle;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberProfileRepositoryCustomImpl implements MemberProfileRepositoryCustom {
    private final JPAQueryFactory queryFactory;

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

        builder.and(eqGender(memberProfile, requestDto.getGender()))
                .and(eqSmokeType(memberStyle, requestDto.getSmokeType()))
                .and(eqDrinkType(memberStyle, requestDto.getDrinkType()))
                .and(eqContactType(memberStyle, requestDto.getContactType()))
                .and(eqDateStyleType(memberStyle, requestDto.getDateStyleType()))
                .and(eqDateCostType(memberStyle, requestDto.getDateCostType()))
                .and(eqMbtiType(memberStyle, requestDto.getMbti()))
                .and(eqJustFriendType(memberStyle, requestDto.getJustFriendType()));

        return queryFactory
                .select(Projections.fields(MemberBookProfileResponseDto.class
                        , memberProfile.member.id.as("memberId")
                        , memberBook.book.id.as("bookId")
                        , memberProfile.nickname.as("memberName")
                        , memberProfile.birthDate.year().subtract(LocalDate.now().getYear() + 1).abs().as("memberAge")
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

    private BooleanBuilder eqGender(QMemberProfile memberProfile, Gender gender) {
        if (gender == null) {
            return null;
        }
        return new BooleanBuilder(memberProfile.gender.eq(gender));
    }

    private BooleanBuilder eqSmokeType(QMemberStyle memberStyle, SmokeType smokeType) {
        if (smokeType == null) {
            return null;
        }
        return new BooleanBuilder(memberStyle.smokeType.eq(smokeType));
    }

    private BooleanBuilder eqDrinkType(QMemberStyle memberStyle, DrinkType drinkType) {
        if (drinkType == null) {
            return null;
        }
        return new BooleanBuilder(memberStyle.drinkType.eq(drinkType));
    }

    private BooleanBuilder eqContactType(QMemberStyle memberStyle, ContactType contactType) {
        if (contactType == null) {
            return null;
        }
        return new BooleanBuilder(memberStyle.contactType.eq(contactType));
    }

    private BooleanBuilder eqDateStyleType(QMemberStyle memberStyle, DateStyleType dateStyleType) {
        if (dateStyleType == null) {
            return null;
        }
        return new BooleanBuilder(memberStyle.dateStyleType.eq(dateStyleType));
    }

    private BooleanBuilder eqDateCostType(QMemberStyle memberStyle, DateCostType dateCostType) {
        if (dateCostType == null) {
            return null;
        }
        return new BooleanBuilder(memberStyle.dateCostType.eq(dateCostType));
    }

    private BooleanBuilder eqMbtiType(QMemberStyle memberStyle, Mbti mbti) {
        if (mbti == null) {
            return null;
        }
        return new BooleanBuilder(memberStyle.mbti.eq(mbti));
    }

    private BooleanBuilder eqJustFriendType(QMemberStyle memberStyle, JustFriendType justFriendType) {
        if (justFriendType == null) {
            return null;
        }
        return new BooleanBuilder(memberStyle.justFriendType.eq(justFriendType));
    }
}
