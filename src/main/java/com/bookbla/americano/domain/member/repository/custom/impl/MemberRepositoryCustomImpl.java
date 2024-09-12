package com.bookbla.americano.domain.member.repository.custom.impl;


import com.bookbla.americano.domain.book.repository.entity.QBook;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.response.BookProfileResponse;
import com.bookbla.americano.domain.member.enums.*;
import com.bookbla.americano.domain.member.repository.custom.MemberRepositoryCustom;
import com.bookbla.americano.domain.member.repository.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    public List<Member> getRecommendationMembers(Member member, MemberBook memberBook) {
        QMember qMember = QMember.member;
        QMemberStyle qMemberStyle = qMember.memberStyle;
        QBook qBook = QMemberBook.memberBook.book;
        QMemberVerify qMemberVerify = QMemberVerify.memberVerify;

        Gender gender = member.getMemberProfile().getGender();
        String schoolName = member.getSchool().getName();
        Set<Long> excludeMemberIds = member.getMemberMatchIgnores();
        SmokeType smokeType = member.getMemberStyle().getSmokeType();

        LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(2);
        LocalDateTime fourteenDaysAgo = LocalDateTime.now().minusDays(14);

        return queryFactory
                .selectFrom(qMember)
                .where(
                        /* 0. 자기 자신은 추천될 수 없다 */
                        qMember.id.ne(member.getId()),

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
                                .and(
                                        qMemberVerify.verifyStatus.eq(MemberVerifyStatus.SUCCESS)
                                        .or(qMemberVerify.verifyStatus.eq(MemberVerifyStatus.PENDING)
                                        .or(qMemberVerify.verifyStatus.eq(MemberVerifyStatus.FAIL)))
                                )))),

                        /* 5. 이전에 사용자가 무시하기를 누르지 않은 사용자만 추천 */
                        qMember.id.notIn(excludeMemberIds))
                        /* 5-1. 다른 책의 경우는 나와도 됨 */
                        /* 5-2. 독서 퀴즈를 틀렸을 경우 다른 책은 나와도 됨 */

                        /* 6. 이미 매칭된 경우 뜨지 않아야 함 -> 다른 책 전부 안나와야 함*/
                        /* -> 매칭된 경우 excludeMemberIds에 추가하기 */

                        /* 7. 이미 매칭되었던(채팅방이 있었던 경우) 100일 뒤에 다시 나와야 함 */
                        /* 8. 엽서를 보내고 대기중인 상태일 떄는 그 사람이 제외되어야 함 */
                            /* 8-1. 엽서를 상대방이 거절한 경우 내 홈에서 2주 후에 다시 나와야 함 */

                .orderBy(
                        new CaseBuilder()

                        /* 1. 같은 학교, 같은 책 */
                        .when(qMember.school.name.eq(schoolName)
                                .and(qBook.isbn.eq(memberBook.getBook().getIsbn())))
                        .then(1)

                        /* 2. 다른 학교, 같은 책 */
                        .when(qMember.school.name.ne(schoolName)
                                .and(qBook.isbn.eq(memberBook.getBook().getIsbn())))
                        .then(2)

                        /* 3. 같은 학교, 같은 작가 (대표 저자) */
                        .when(qMember.school.name.eq(schoolName)
                                .and(qBook.authors.get(0).eq(memberBook.getBook().getAuthors().get(0))))
                        .then(3)

                        /* 4. 다른 학교, 같은 작가 (대표 저자) */
                        .when(qMember.school.name.ne(schoolName)
                                .and(qBook.authors.get(0).eq(memberBook.getBook().getAuthors().get(0))))
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
