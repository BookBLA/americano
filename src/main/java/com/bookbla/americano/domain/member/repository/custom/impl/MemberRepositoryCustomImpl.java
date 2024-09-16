package com.bookbla.americano.domain.member.repository.custom.impl;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.repository.entity.QBook;
import com.bookbla.americano.domain.chat.repository.entity.QChatRoom;
import com.bookbla.americano.domain.chat.repository.entity.QMemberChatRoom;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.BookProfileResponse;
import com.bookbla.americano.domain.member.enums.*;
import com.bookbla.americano.domain.member.exception.MemberBookExceptionType;
import com.bookbla.americano.domain.member.repository.custom.MemberRepositoryCustom;
import com.bookbla.americano.domain.member.repository.entity.*;

import com.bookbla.americano.domain.member.service.MemberPostcardService;
import com.bookbla.americano.domain.member.service.dto.MemberRecommendationDto;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bookbla.americano.domain.book.repository.entity.QBook.book;
import static com.bookbla.americano.domain.member.repository.entity.QMember.member;
import static com.bookbla.americano.domain.member.repository.entity.QMemberBook.memberBook;
import static com.bookbla.americano.domain.member.repository.entity.QMemberMatch.memberMatch;
import static com.bookbla.americano.domain.member.repository.entity.QMemberStyle.memberStyle;
import static com.bookbla.americano.domain.member.repository.entity.QMemberVerify.memberVerify;
import static com.bookbla.americano.domain.school.repository.entity.QSchool.school;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final MemberPostcardService memberPostcardService;

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
        QMemberStyle qMemberStyle = memberStyle;
        QBook qBook = book;
        QMemberVerify qMemberVerify = memberVerify;
        QMemberMatch qMemberMatch = memberMatch;

        // 앱 사용자 정보
        Long memberId = recommendationDto.getMemberId();
        Gender gender = recommendationDto.getMemberGender();
        Set<Long> excludeMemberIds = recommendationDto.getExcludeMemberIds();
        String schoolName = recommendationDto.getMemberSchoolName();
        SmokeType smokeType = recommendationDto.getMemberSmokeType();
        List<String> bookTitles = recommendationDto.getRecommendationBookDtoList().stream()
                .map(MemberRecommendationDto.RecommendationBookDto::getBookTitle)
                .collect(Collectors.toList());
        List<String> firstAuthorsList = recommendationDto.getRecommendationBookDtoList().stream()
                .map(bookDto -> {
                    List<String> authors = bookDto.getBookAuthors();
                    if (authors.isEmpty()) {
                        throw new BaseException(MemberBookExceptionType.NOT_FOUND_BOOK_AUTHORS);
                    }
                    return authors.get(0);
                })
                .collect(Collectors.toList());

        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);
        LocalDateTime fourteenDaysAgo = LocalDateTime.now().minusDays(14);

        /* 6 조건 */
        List<Long> receiveByIdsWithAccept = memberPostcardService.getReceiveByIds(memberId, PostcardStatus.ACCEPT);
        /* 8 조건 */
        List<Long> receiveByIdsWithPending = memberPostcardService.getReceiveByIds(memberId, PostcardStatus.PENDING);
        /* 8-1 조건 */
        List<Long> receiveByIdsWithRefused = memberPostcardService.getReceiveByIdsRefused(memberId, postcards);

        return queryFactory
                .select(qMember.id, qBook.id)
                .from(qMember)
                .join(qMemberBook.book, qBook)
                .leftJoin(qMemberMatch)
                .where(
                        /* 0. 자기 자신은 추천될 수 없다 */
                        qMember.id.ne(memberId),

                        /* 1. 이성만 추천 */
                        qMember.memberProfile.gender.ne(gender),

                        /* 2. 마지막 로그인으로부터 14일 지나지 않은 사용자만 추천 */
                        qMember.lastLoginAt.after(fourteenDaysAgo),

                        /* 3. 최초 가입 후 이틀이 지났다면 학생증이 인증된 상태만 추천 */
                        ((qMember.createdAt.before(twoDaysAgo).or(qMember.createdAt.eq(twoDaysAgo))
                                .and(qMemberVerify.verifyType.eq(MemberVerifyType.STUDENT_ID))
                                .and(qMemberVerify.verifyStatus.eq(MemberVerifyStatus.SUCCESS)))),

                        /* 4. 이틀이 지나지 않았다면 그 외 상태도 가능 */
                        ((qMember.createdAt.after(twoDaysAgo).or(qMember.createdAt.eq(twoDaysAgo))
                                .and(qMemberVerify.verifyType.eq(MemberVerifyType.STUDENT_ID)
                                        .and(qMemberVerify.verifyStatus.eq(MemberVerifyStatus.SUCCESS))
                                        .or(qMemberVerify.verifyStatus.eq(MemberVerifyStatus.PENDING))
                                        .or(qMemberVerify.verifyStatus.eq(MemberVerifyStatus.FAIL))
                                ))),

                        /* 5. 이전에 사용자가 무시하기를 누르지 않은 사용자만 추천 */
                        /* -> 무시했을시 excludeBookIds에 추가 */
                        qMember.id.notIn(excludeMemberIds),

                        /* 5-1. 다른 책의 경우는 나와도 됨 */
                        /* 5-2. 독서 퀴즈를 틀렸을 경우 다른 책은 나와도 됨 */
                        qMemberMatch.passedMember.id.isNull(),

                        /* 6. 이미 매칭된 경우 뜨지 않아야 함 -> 다른 책 전부 안나와야 함*/
                        qMember.id.notIn(receiveByIdsWithAccept),
                        /* -> 매칭된 경우 excludeMemberIds에 추가하기 */

                        /* 7. 이미 매칭되었던(채팅방이 있었던 경우) 100일 뒤에 다시 나와야 함 */

                        /* 8. 엽서를 보내고 대기중인 상태일 떄는 그 사람이 제외되어야 함 */
                        qMember.id.notIn(receiveByIdsWithPending),

                        /* 8-1. 엽서를 상대방이 거절한 경우 내 홈에서 2주 후에 다시 나와야 함 */
                        qMember.id.in(receiveByIdsWithRefused)
                )
                .orderBy(
                        new CaseBuilder()

                                /* 1. 같은 학교, 같은 책 */
                                .when(qMember.school.name.eq(schoolName)
                                        .and(qBook.title.in(bookTitles)))
                                .then(1)

                                /* 2. 다른 학교, 같은 책 */
                                .when(qMember.school.name.ne(schoolName)
                                        .and(qBook.title.in(bookTitles)))
                                .then(2)

                                /* 3. 같은 학교, 같은 작가 (대표 저자) */
                                .when(qMember.school.name.eq(schoolName)
                                        .and(qBook.authors.get(0).in(firstAuthorsList)))
                                .then(3)

                                /* 4. 다른 학교, 같은 작가 (대표 저자) */
                                .when(qMember.school.name.ne(schoolName)
                                        .and(qBook.authors.get(0).in(firstAuthorsList)))
                                .then(4)

                                /* 5. 같은 학교, 흡연 여부 동일 */
                                .when(qMember.school.name.eq(schoolName)
                                        .and(qMemberStyle.smokeType.eq(smokeType)))
                                .then(5)

                                /* 6. 다른 학교, 흡연 여부 동일 */
                                .when(qMember.school.name.ne(schoolName)
                                        .and(qMemberStyle.smokeType.eq(smokeType)))
                                .then(6)

                                .otherwise(7)
                                .asc()
                )
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
                .innerJoin(qMember).on(qMemberChatRoom.member.eq(qMember)).fetchJoin()
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
