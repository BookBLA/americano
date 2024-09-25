package com.bookbla.americano.domain.postcard.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.repository.MemberBlockRepository;
import com.bookbla.americano.domain.member.repository.MemberBookmarkRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBlock;
import com.bookbla.americano.domain.member.repository.entity.MemberBookmark;
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
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.bookbla.americano.domain.postcard.enums.PostcardStatus.REFUSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PostcardServiceTest {

    @Autowired
    private PostcardService postcardService;

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

    private PostcardType postcardType;

    @BeforeEach
    void setUp() {
        postcardType = postcardTypeRepository.save(PostcardType.builder().build());
    }

    @Test
    void 엽서를_보낼_수_있다() {
        //given
        Member sendMember = memberRepository.save(Member.builder().build());
        Member reciveMember = memberRepository.save(Member.builder().build());
        MemberBookmark memberBookmark = MemberBookmark.builder()
                .member(sendMember)
                .bookmarkCount(100).build();
        bookmarkRepository.save(memberBookmark);

        SendPostcardRequest request = new SendPostcardRequest(postcardType.getId(), reciveMember.getId(), "memberReply");

        //when
        SendPostcardResponse response = postcardService.send(sendMember.getId(), request);

        //then
        assertThat(response.getIsSendSuccess()).isTrue();
    }

    @Test
    void 북마크_개수가_부족하면_엽서를_보낼_수_없다() {
        //given
        Member sendMember = memberRepository.save(Member.builder().build());
        Member reciveMember = memberRepository.save(Member.builder().build());
        MemberBookmark memberBookmark = MemberBookmark.builder()
                .member(sendMember)
                .bookmarkCount(10).build();
        bookmarkRepository.save(memberBookmark);

        SendPostcardRequest request = new SendPostcardRequest(postcardType.getId(), reciveMember.getId(), "memberReply");

        // when & then
        assertThatThrownBy(() -> postcardService.send(sendMember.getId(), request))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("책갈피 개수가 부족합니다.");
    }

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
        assertThatThrownBy(() -> postcardService.validateSendPostcard(blockedMember.getId(), blockerMember.getId()))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(PostcardExceptionType.BLOCKED.getMessage());
    }

    @EnumSource(mode = INCLUDE, names = {"PENDING", "ACCEPT", "ALL_WRONG", "READ"})
    @ParameterizedTest(name = "엽서를_보낼_수_없다면_예외를_반환한다")
    void 엽서를_보낼_수_없다면_예외를_반환한다(PostcardStatus postcardStatus) {
        // given
        Member sendMember = memberRepository.save(Member.builder().build());
        Member reciveMember = memberRepository.save(Member.builder().build());

        postcardRepository.save(Postcard.builder()
                .sendMember(sendMember)
                .receiveMember(reciveMember)
                .postcardStatus(postcardStatus)
                .build());

        // when, then
        assertThatThrownBy(() -> postcardService.validateSendPostcard(sendMember.getId(), reciveMember.getId()))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(" 엽서가 존재합니다");
    }

    @Test
    void 기존에_보낸_엽서가_존재하지_않는다면_엽서를_전송할_수_있다() {
        // given
        Member sendMember = memberRepository.save(Member.builder().build());
        Member reciveMember = memberRepository.save(Member.builder().build());

        // when
        PostcardSendValidateResponse response = postcardService.validateSendPostcard(sendMember.getId(), reciveMember.getId());

        // then
        assertThat(response.getIsRefused()).isFalse();
    }

    @Test
    void 기존에_보낸_엽서가_거절되었다면_엽서를_전송할_수_있다() {
        // given
        Member sendMember = memberRepository.save(Member.builder().build());
        Member reciveMember = memberRepository.save(Member.builder().build());

        postcardRepository.save(Postcard.builder()
                .sendMember(sendMember)
                .receiveMember(reciveMember)
                .postcardStatus(REFUSED)
                .build());

        // when
        PostcardSendValidateResponse response = postcardService.validateSendPostcard(sendMember.getId(), reciveMember.getId());

        // then
        assertThat(response.getIsRefused()).isTrue();
    }

    @AfterEach
    void tearDown() {
        quizQuestionRepository.deleteAllInBatch();
        bookmarkRepository.deleteAllInBatch();
        postcardRepository.deleteAllInBatch();
        postcardTypeRepository.deleteAllInBatch();
        memberBlockRepository.deleteAllInBatch();
    }
}
