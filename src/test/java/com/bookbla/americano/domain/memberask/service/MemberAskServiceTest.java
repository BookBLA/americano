package com.bookbla.americano.domain.memberask.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.Member;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.memberask.controller.dto.request.MemberAskCreateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.response.MemberAskResponse;
import com.bookbla.americano.domain.memberask.repository.MemberAskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemberAskServiceTest {

    @Autowired
    private MemberAskService memberAskService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberAskRepository memberAskRepository;

    @Test
    void 회원의_개인_질문을_생성할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());
        MemberAskCreateRequest memberAskCreateRequest = new MemberAskCreateRequest("주로 어디서 책을 읽는 편이에요?");

        // when
        MemberAskResponse memberAskResponse = memberAskService.createMemberAsk(member.getId(),
                memberAskCreateRequest);

        // then
        assertThat(memberAskResponse.getMemberAskResponseId()).isNotNull();
    }

    @Test
    void 회원의_개인_질문이_제한_길이를_넘어가면_예외가_발생한다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());
        MemberAskCreateRequest memberAskCreateRequest = new MemberAskCreateRequest(" ".repeat(81));

        // when, then
        assertThatThrownBy(() -> memberAskService.createMemberAsk(member.getId(), memberAskCreateRequest))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("개인 질문은 공백 포함 80자 이하로만 작성가능합니다");
    }

    @Test
    void 존재하지_않는_회원의_개인_질문을_생성할_경우_예외가_발생한다() {
        // given
        Long nonMemberId = -1L;
        MemberAskCreateRequest memberAskCreateRequest = new MemberAskCreateRequest("주로 어디서 책을 읽는 편이에요?");

        // when, then
        assertThatThrownBy(() -> memberAskService.createMemberAsk(nonMemberId, memberAskCreateRequest))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("해당 식별자를 가진 회원이 존재하지 않습니다");
    }

    @AfterEach
    void cleanUp() {
        memberAskRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
