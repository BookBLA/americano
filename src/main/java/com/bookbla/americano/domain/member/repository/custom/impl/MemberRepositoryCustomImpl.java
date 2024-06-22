package com.bookbla.americano.domain.member.repository.custom.impl;


import com.querydsl.core.types.dsl.BooleanExpression;
import java.time.LocalDate;
import java.util.List;

import com.bookbla.americano.domain.book.repository.entity.QBook;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.BookProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponse;
import com.bookbla.americano.domain.member.enums.ContactType;
import com.bookbla.americano.domain.member.enums.DateCostType;
import com.bookbla.americano.domain.member.enums.DateStyleType;
import com.bookbla.americano.domain.member.enums.DrinkType;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.JustFriendType;
import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.SmokeType;
import com.bookbla.americano.domain.member.repository.custom.MemberRepositoryCustom;
import com.bookbla.americano.domain.member.repository.entity.QMember;
import com.bookbla.americano.domain.member.repository.entity.QMemberBook;
import com.bookbla.americano.domain.member.repository.entity.QMemberProfile;
import com.bookbla.americano.domain.member.repository.entity.QMemberStyle;
import com.bookbla.americano.domain.member.repository.entity.QMemberBlock;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberBookProfileResponse> searchSameBookMember(Long memberId, MemberBookProfileRequestDto requestDto) {
        QMember member = QMember.member;
        QMemberBook memberBook = QMemberBook.memberBook;
        QBook book = QBook.book;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(memberBook.book.id.in(
                        JPAExpressions
                                .select(memberBook.book.id)
                                .from(member)
                                .innerJoin(memberBook).on(member.eq(memberBook.member))
                                .where(member.id.eq(memberId)
                                        .and(memberBook.isDeleted.eq(false)))));

        builder.and(eqGender(member.memberProfile, requestDto.getGender()))
                .and(eqSmokeType(member.memberStyle, requestDto.getSmokeType()))
                .and(eqDrinkType(member.memberStyle, requestDto.getDrinkType()))
                .and(eqContactType(member.memberStyle, requestDto.getContactType()))
                .and(eqDateStyleType(member.memberStyle, requestDto.getDateStyleType()))
                .and(eqDateCostType(member.memberStyle, requestDto.getDateCostType()))
                .and(eqMbtiType(member.memberStyle, requestDto.getMbti()))
                .and(eqJustFriendType(member.memberStyle, requestDto.getJustFriendType()));

        return queryFactory
                .select(Projections.fields(MemberBookProfileResponse.class
                        , member.id.as("memberId")
                        , memberBook.book.id.as("bookId")
                        , member.memberProfile.name.as("memberName")
                        , member.memberProfile.birthDate.year().subtract(LocalDate.now().getYear() + 1).abs().as("memberAge")
                        , member.memberProfile.gender.as("memberGender")
                        , member.memberProfile.schoolName.as("memberSchoolName")
                        , book.title.as("bookName")
                        , book.imageUrl.as("bookImageUrl")
                        , memberBook.isRepresentative.as("bookIsRepresentative"))
                )
                .from(member)
                .innerJoin(memberBook).on(member.eq(memberBook.member))
                .innerJoin(book).on(memberBook.book.eq(book))
                .where(builder.or(member.id.eq(memberId)))
                .fetch();
    }

    @Override
    public List<BookProfileResponse> getAllMembers(Long memberId, MemberBookProfileRequestDto requestDto) {
        QMember member = QMember.member;
        QMemberBook memberBook = QMemberBook.memberBook;
        QMemberBlock memberBlock = QMemberBlock.memberBlock;
        QBook book = QBook.book;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(eqGender(member.memberProfile, requestDto.getGender()))
                .and(eqSmokeType(member.memberStyle, requestDto.getSmokeType()))
                .and(eqDrinkType(member.memberStyle, requestDto.getDrinkType()))
                .and(eqContactType(member.memberStyle, requestDto.getContactType()))
                .and(eqDateStyleType(member.memberStyle, requestDto.getDateStyleType()))
                .and(eqDateCostType(member.memberStyle, requestDto.getDateCostType()))
                .and(eqMbtiType(member.memberStyle, requestDto.getMbti()))
                .and(eqJustFriendType(member.memberStyle, requestDto.getJustFriendType()))
                .and(new BooleanBuilder(memberBook.isRepresentative.eq(Boolean.TRUE)));

        // memberId가 차단해서 차단당한 멤버 ID List
        List<Long> blockedByIds = queryFactory
            .select(memberBlock.blockedMember.id)
            .from(memberBlock)
            .where(memberBlock.blockerMember.id.eq(memberId))
            .fetch();

        // memberId를 차단한 유저의 ID 목록 List
        List<Long> blockerIds = queryFactory
            .select(memberBlock.blockerMember.id)
            .from(memberBlock)
            .where(memberBlock.blockedMember.id.eq(memberId))
            .fetch();

        return queryFactory
                .select(Projections.fields(BookProfileResponse.class
                        , member.id.as("memberId")
                        , memberBook.book.id.as("bookId")
                        , member.memberProfile.name.as("memberName")
                        , member.memberProfile.birthDate.as("memberBirthDate")
                        , member.memberProfile.gender.as("memberGender")
                        , member.memberProfile.schoolName.as("memberSchoolName")
                        , book.title.as("bookName")
                        , book.imageUrl.as("bookImageUrl")
                        , memberBook.isRepresentative.as("bookIsRepresentative"))
                )
                .from(member)
                .innerJoin(memberBook).on(member.eq(memberBook.member))
                .innerJoin(book).on(memberBook.book.eq(book).and(memberBook.isDeleted.eq(false)))
                .where(builder.andNot(member.id.eq(memberId))
                        .and(member.memberStatus.eq(MemberStatus.COMPLETED))
                        .and(member.id.notIn(blockerIds))
                        .and(member.id.notIn(blockedByIds))
                )
                .orderBy(member.createdAt.desc())
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
