package com.bookbla.americano.domain.postcard.repository.custom.impl;

import java.util.List;

import com.bookbla.americano.domain.book.repository.entity.QBook;
import com.bookbla.americano.domain.member.repository.entity.QMember;
import com.bookbla.americano.domain.member.repository.entity.QMemberBook;
import com.bookbla.americano.domain.memberask.repository.entity.QMemberAsk;
import com.bookbla.americano.domain.memberask.repository.entity.QMemberReply;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.custom.PostcardRepositoryCustom;
import com.bookbla.americano.domain.postcard.repository.entity.QPostcard;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardFromResponse;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardToResponse;
import com.bookbla.americano.domain.quiz.repository.entity.QQuizQuestion;
import com.bookbla.americano.domain.quiz.repository.entity.QQuizReply;
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
                        , postcard.receiveMember.memberProfile.profileImageUrl.as("memberProfileImageUrl")
                        , postcard.receiveMember.memberProfile.openKakaoRoomUrl.as("memberOpenKakaoRoomUrl")
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
    public List<PostcardToResponse> getPostcardsToMember(Long memberId) {
        QMember member = QMember.member;
        QMemberBook memberBook = QMemberBook.memberBook;
        QPostcard postcard = QPostcard.postcard;
        QBook book = QBook.book;
        QQuizQuestion quizQuestion = QQuizQuestion.quizQuestion;
        QQuizReply quizReply = QQuizReply.quizReply;
        QMemberAsk memberAsk = QMemberAsk.memberAsk;
        QMemberReply memberReply = QMemberReply.memberReply;

        return queryFactory.select(Projections.fields(PostcardToResponse.class
                        , postcard.id.as("postcardId")
                        , postcard.sendMember.id.as("memberId")
                        , postcard.sendMember.memberProfile.name.as("memberName")
                        , postcard.sendMember.memberProfile.birthDate.as("memberBirthDate")
                        , postcard.sendMember.memberProfile.gender.as("memberGender")
                        , member.school.name.as("memberSchoolName")
                        , postcard.sendMember.memberProfile.profileImageUrl.as("memberProfileImageUrl")
                        , postcard.sendMember.memberProfile.openKakaoRoomUrl.as("memberKakaoRoomUrl")
                        , postcard.sendMember.memberStyle.drinkType
                        , postcard.sendMember.memberStyle.smokeType
                        , postcard.sendMember.memberStyle.contactType
                        , postcard.sendMember.memberStyle.dateStyleType
                        , postcard.sendMember.memberStyle.dateCostType
                        , postcard.sendMember.memberStyle.mbti
                        , postcard.sendMember.memberStyle.justFriendType
                        , postcard.sendMember.memberStyle.heightType
                        , postcard.postcardStatus
                        , postcard.postcardType.imageUrl.as("postcardImageUrl")
                        , book.title.as("bookTitle")
                        , book.imageUrl.as("bookImageUrl")
                        , quizReply.correctStatus
                        , memberReply.content.as("memberReplyContent"))
                )
                .from(postcard)
                .innerJoin(quizReply).on(postcard.eq(quizReply.postcard))
                .innerJoin(quizQuestion).on(quizReply.quizQuestion.eq(quizQuestion))
                .innerJoin(memberBook).on(quizQuestion.memberBook.eq(memberBook))
                .innerJoin(book).on(memberBook.book.eq(book))
                .innerJoin(member).on(memberBook.member.eq(member))
                .innerJoin(memberAsk).on(member.eq(memberAsk.member))
                .innerJoin(memberReply).on(postcard.memberReply.eq(memberReply))
                .where(postcard.receiveMember.id.eq(memberId)
                        .and(postcard.postcardStatus.eq(PostcardStatus.PENDING)
                                .or(postcard.postcardStatus.eq(PostcardStatus.READ))
                                .or(postcard.postcardStatus.eq(PostcardStatus.ACCEPT))))
                .orderBy(postcard.sendMember.id.asc())
                .fetch();
    }
}
