package com.bookbla.americano.domain.postcard.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.postcard.controller.dto.response.PostcardSendValidateResponse;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.repository.PostcardRepository;
import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
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
        postcardRepository.deleteAll();
    }
}
