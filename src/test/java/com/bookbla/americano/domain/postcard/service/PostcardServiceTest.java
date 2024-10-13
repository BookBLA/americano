package com.bookbla.americano.domain.postcard.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.book.repository.BookRepository;
import com.bookbla.americano.domain.book.repository.entity.Book;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.matching.repository.MemberMatchingRepository;
import com.bookbla.americano.domain.matching.repository.entity.MemberMatching;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.repository.MemberBlockRepository;
import com.bookbla.americano.domain.member.repository.MemberBookRepository;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBlock;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import com.bookbla.americano.domain.postcard.controller.dto.response.PostcardSendValidateResponse;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.exception.PostcardExceptionType;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import com.bookbla.americano.domain.postcard.repository.PostcardTypeRepository;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import com.bookbla.americano.domain.postcard.repository.entity.PostcardType;
import com.bookbla.americano.domain.postcard.service.dto.request.SendPostcardRequest;
import com.bookbla.americano.domain.postcard.service.dto.response.SendPostcardResponse;
import com.bookbla.americano.domain.quiz.repository.QuizQuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.bookbla.americano.domain.member.exception.MemberProfileExceptionType.STUDENT_ID_NOT_VALID;
import static com.bookbla.americano.domain.postcard.enums.PostcardStatus.PENDING;
import static com.bookbla.americano.domain.postcard.enums.PostcardStatus.REFUSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PostcardServiceTest {

    @Autowired
    private PostcardService sut;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostcardRepository postcardRepository;

    @Autowired
    private MemberBlockRepository memberBlockRepository;

    @Autowired
    private MemberBookmarkRepository bookmarkRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private PostcardTypeRepository postcardTypeRepository;

    @Autowired
    private MemberBookRepository memberBookRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberMatchingRepository memberMatchingRepository;

    private PostcardType postcardType;

    @BeforeEach
    void setUp() {
        postcardType = postcardTypeRepository.save(PostcardType.builder().build());
    }

    @Test
    void 학생증_인증을_하지_않으면_엽서를_보낼_수_없다() {
        MemberProfile memberProfile = MemberProfile.builder().studentIdImageStatus(StudentIdImageStatus.UNREGISTER).build();
        Member sendMember = memberRepository.save(Member.builder().memberProfile(memberProfile).build());
        Member receiveMember = memberRepository.save(Member.builder().build());
        Book book = bookRepository.save(Book.builder().build());
        MemberBook receiveMemberBook = memberBookRepository.save(MemberBook.builder().member(receiveMember).book(book).build());
        MemberBookmark memberBookmark = MemberBookmark.builder()
                .member(sendMember)
                .bookmarkCount(100).build();
        bookmarkRepository.save(memberBookmark);
        String channelUrl = "aaabbbccc";
        SendPostcardRequest request = new SendPostcardRequest(postcardType.getId(), receiveMember.getId(), receiveMemberBook.getId(), "memberReply", channelUrl);

        //when & then
        assertThatThrownBy(() -> sut.send(sendMember.getId(), request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("학생증이 인증되지 않은 회원입니다.");
    }

    @Test
    void 엽서를_보낼_수_있다() {
        //given
        MemberProfile memberProfile = MemberProfile.builder().studentIdImageStatus(StudentIdImageStatus.DONE).build();
        Member sendMember = memberRepository.save(Member.builder().memberProfile(memberProfile).build());
        Member receiveMember = memberRepository.save(Member.builder().build());
        Book book = bookRepository.save(Book.builder().build());
        MemberBook receiveMemberBook = memberBookRepository.save(MemberBook.builder().member(receiveMember).book(book).build());
        MemberBookmark memberBookmark = MemberBookmark.builder()
                .member(sendMember)
                .bookmarkCount(100).build();
        bookmarkRepository.save(memberBookmark);
        memberMatchingRepository.save(MemberMatching.builder()
                .member(sendMember)
                .currentMatchedMemberId(receiveMember.getId())
                .currentMatchedMemberBookId(receiveMemberBook.getId())
                .isInvitationCard(false)
                .build());
        String channelUrl = "aaabbbccc";
        SendPostcardRequest request = new SendPostcardRequest(postcardType.getId(), receiveMember.getId(), receiveMemberBook.getId(), "memberReply", channelUrl);

        //when
        SendPostcardResponse response = sut.send(sendMember.getId(), request);

        //then
        assertThat(response.getIsSendSuccess()).isTrue();
        MemberMatching updatedMemberMatching = memberMatchingRepository.findByMember(sendMember)
                .orElseThrow(() -> new BaseException(MemberMatchingExceptionType.MEMBER_MATCHING_NOT_FOUND));
        assertThat(updatedMemberMatching.getIsInvitationCard()).isTrue();
    }

    @Test
    void 북마크_개수가_부족하면_엽서를_보낼_수_없다() {
        //given
        MemberProfile memberProfile = MemberProfile.builder().studentIdImageStatus(StudentIdImageStatus.DONE).build();
        Member sendMember = memberRepository.save(Member.builder().memberProfile(memberProfile).build());
        Member receiveMember = memberRepository.save(Member.builder().build());
        Book book = bookRepository.save(Book.builder().build());
        MemberBook receiveMemberBook = memberBookRepository.save(MemberBook.builder().member(receiveMember).book(book).build());
        MemberBookmark memberBookmark = MemberBookmark.builder()
                .member(sendMember)
                .bookmarkCount(10).build();
        bookmarkRepository.save(memberBookmark);
        String channelUrl = "aaabbbccc";
        SendPostcardRequest request = new SendPostcardRequest(postcardType.getId(), receiveMember.getId(), receiveMemberBook.getId(), "memberReply", channelUrl);

        // when & then
        assertThatThrownBy(() -> sut.send(sendMember.getId(), request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("책갈피 개수가 부족합니다.");
    }

    @Nested
    class 엽서_전송_검증 {

        @Nested
        class 성공 {

            @Test
            void 기존에_보낸_엽서가_존재하지_않는다면_엽서를_전송할_수_있다() {
                // given
                Member sendMember = memberRepository.save(Member.builder().build());
                Member reciveMember = memberRepository.save(Member.builder().build());

                // when
                PostcardSendValidateResponse response = sut.validateSendPostcard(sendMember.getId(), reciveMember.getId());

                // then
                assertThat(response.getIsRefused()).isFalse();
            }

            @Test
            void 기존에_보낸_엽서가_거절되었다면_엽서를_전송할_수_있다() {
                // given
                Member sendMember = memberRepository.save(Member.builder().build());
                Member receiveMember = memberRepository.save(Member.builder().build());

                postcardRepository.save(Postcard.builder()
                        .sendMember(sendMember)
                        .receiveMember(receiveMember)
                        .postcardStatus(REFUSED)
                        .build());

                // when
                PostcardSendValidateResponse response = sut.validateSendPostcard(sendMember.getId(), receiveMember.getId());

                // then
                assertThat(response.getIsRefused()).isTrue();
            }
        }

        @Nested
        class 실패 {

            @Test
            void 받는_사람이_보내는_사람을_차단했다면_엽서를_보낼_수_없다() {
                // given
                Member blockerMember = memberRepository.save(Member.builder().build());
                Member blockedMember = memberRepository.save(Member.builder().build());

                memberBlockRepository.save(MemberBlock.builder()
                        .blockedMember(blockedMember)
                        .blockerMember(blockerMember)
                        .build());

                // when, then
                assertThatThrownBy(() -> sut.validateSendPostcard(blockedMember.getId(), blockerMember.getId()))
                        .isInstanceOf(BaseException.class)
                        .hasMessageContaining(PostcardExceptionType.BLOCKED.getMessage());
            }

            @EnumSource(mode = INCLUDE, names = {"PENDING", "ACCEPT", "ALL_WRONG", "READ"})
            @ParameterizedTest(name = "기존_전송한_엽서의_상태에_따라_새로_엽서를_전송할_수_없다")
            void 기존_전송한_엽서의_상태에_따라_새로_엽서를_전송할_수_없다(PostcardStatus postcardStatus) {
                // given
                Member sendMember = memberRepository.save(Member.builder().build());
                Member receiveMember = memberRepository.save(Member.builder().build());

                postcardRepository.save(Postcard.builder()
                        .sendMember(sendMember)
                        .receiveMember(receiveMember)
                        .postcardStatus(postcardStatus)
                        .build());

                // when, then
                assertThatThrownBy(() -> sut.validateSendPostcard(sendMember.getId(), receiveMember.getId()))
                        .isInstanceOf(BaseException.class);
            }
        }
    }

    @Nested
    class 엽서_읽기 {

        @Test
        void 학생증_인증이_되지_않은_회원은_엽서를_읽을_수_없다() {
            // given
            Member sendMember = memberRepository.save(Member.builder().build());
            Member studentIdUnregisterdMember = memberRepository.save(Member.builder()
                    .memberProfile(MemberProfile.builder().studentIdImageStatus(StudentIdImageStatus.UNREGISTER).build())
                    .memberStatus(MemberStatus.APPROVAL)
                    .build());

            Postcard postcard = postcardRepository.save(Postcard.builder()
                    .sendMember(sendMember)
                    .receiveMember(studentIdUnregisterdMember)
                    .postcardStatus(PENDING)
                    .build());

            // when, then
            assertThatThrownBy(() -> sut.readMemberPostcard(studentIdUnregisterdMember.getId(), postcard.getId()))
                    .isInstanceOf(BaseException.class)
                    .hasMessageContaining(STUDENT_ID_NOT_VALID.getMessage());
        }

    }


    @AfterEach
    void tearDown() {
        quizQuestionRepository.deleteAllInBatch();
        bookmarkRepository.deleteAllInBatch();
        postcardRepository.deleteAllInBatch();
        postcardTypeRepository.deleteAllInBatch();
        memberBlockRepository.deleteAllInBatch();
        memberBookRepository.deleteAllInBatch();
        memberMatchingRepository.deleteAllInBatch();
    }
}
