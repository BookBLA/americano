package com.bookbla.americano.domain.member.repository.custom.impl;

import com.bookbla.americano.domain.book.repository.entity.QBook;
import com.bookbla.americano.domain.chat.repository.entity.QChatRoom;
import com.bookbla.americano.domain.chat.repository.entity.QMemberChatRoom;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.BookProfileResponse;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.Mbti;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.SmokeType;
import com.bookbla.americano.domain.member.repository.custom.MemberRepositoryCustom;
import com.bookbla.americano.domain.member.repository.entity.*;
import com.bookbla.americano.domain.member.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bookbla.americano.domain.book.repository.entity.QBook.book;
import static com.bookbla.americano.domain.member.repository.entity.QMember.member;
import static com.bookbla.americano.domain.member.repository.entity.QMemberBook.memberBook;
import static com.bookbla.americano.domain.school.repository.entity.QSchool.school;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private static final int MAX_RANDOM_MATCHING_COUNT = 10;

    @Override
    public List<BookProfileResponse> getAllMembers(Long memberId, MemberBookProfileRequestDto requestDto) {
        QMember member = QMember.member;
        QMemberBook memberBook = QMemberBook.memberBook;
        QMemberBlock memberBlock = QMemberBlock.memberBlock;
        QBook book = QBook.book;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(eqGender(member.memberProfile, requestDto.getGender()))
                .and(eqSmokeType(member.memberStyle, requestDto.getSmokeType()))
                .and(eqMbtiType(member.memberStyle, requestDto.getMbti()));

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
                        , member.school.name.as("memberSchoolName")
                        , book.title.as("bookName")
                        , book.imageUrl.as("bookImageUrl"))
                )
                .from(member)
                .innerJoin(memberBook).on(member.eq(memberBook.member))
                .innerJoin(school).on(member.school.eq(school))
                .innerJoin(book).on(memberBook.book.eq(book).and(memberBook.isDeleted.eq(false)))
                .where(builder.andNot(member.id.eq(memberId))
                        .and(member.memberStatus.eq(MemberStatus.COMPLETED))
                        .and(member.id.notIn(blockerIds))
                        .and(member.id.notIn(blockedByIds))
                )
                .orderBy(member.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Map<Long, Long>> getRecommendationMemberIdsAndBookIds(MemberRecommendationDto recommendationDto, List<Postcard> postcards) {
        QMember qMember = member;
        QMemberBook qMemberBook = memberBook;
        QBook qBook = book;

        // 앱 사용자 정보
        Long memberId = recommendationDto.getMemberId();
        String gender = recommendationDto.getMemberGender();

        return queryFactory
                .select(qMember.id, qBook.id)
                .from(qMember)
                .join(qMemberBook.member, qMember)
                .join(qMemberBook.book, qBook)
                .where(
                        /* 0. 자기 자신은 추천될 수 없다 */
                        qMember.id.ne(memberId),

                        /* 1. 이성만 추천 */
                        qMember.memberProfile.gender.ne(Gender.valueOf(gender)))
                .limit(MAX_RANDOM_MATCHING_COUNT)
                .fetch()
                .stream()
                .map(m -> {
                    Map<Long, Long> map = new HashMap<>();
                    map.put(m.get(qMember.id), m.get(qBook.id));
                    return map;
                })
                .collect(Collectors.toList());
    }

    public List<Member> findByChatroomId(Long chatRoomId) {
        QMember qMember = QMember.member;
        QMemberChatRoom qMemberChatRoom = QMemberChatRoom.memberChatRoom;
        QChatRoom qChatRoom = QChatRoom.chatRoom;

        return queryFactory.select(qMember)
                .from(qChatRoom)
                .innerJoin(qMemberChatRoom).on(qMemberChatRoom.chatRoom.eq(qChatRoom))
                .innerJoin(qMember).on(qMemberChatRoom.member.eq(qMember))
                .where(qChatRoom.id.eq(chatRoomId))
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

    private BooleanBuilder eqMbtiType(QMemberStyle memberStyle, Mbti mbti) {
        if (mbti == null) {
            return null;
        }
        return new BooleanBuilder(memberStyle.mbti.eq(mbti));
    }
}
