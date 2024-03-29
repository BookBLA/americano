package com.bookbla.americano.domain.memberask.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.memberask.controller.dto.request.MemberAskCreateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.request.MemberAskUpdateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.response.MemberAskResponse;
import com.bookbla.americano.domain.memberask.repository.MemberAskRepository;
import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
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
    void 회원의_개인_질문_생성시_제한_길이를_넘어가면_예외가_발생한다() {
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

    @Test
    void 회원의_개인_질문을_조회할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());
        MemberAsk memberAsk = MemberAsk.builder().member(member).contents("주로 어디서 책을 읽는 편이세요?")
                .build();
        memberAskRepository.save(memberAsk);

        // when
        MemberAskResponse memberAskResponse = memberAskService.readMemberAsk(member.getId());

        // then
        assertThat(memberAskResponse.getContents()).isEqualTo("주로 어디서 책을 읽는 편이세요?");
    }


    @Test
    void 회원의_개인_질문을_수정할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());
        MemberAsk memberAsk = MemberAsk.builder().member(member).contents("주로 어디서 책을 읽는 편이세요?").build();
        memberAskRepository.save(memberAsk);

        MemberAskUpdateRequest memberAskUpdateRequest = new MemberAskUpdateRequest("어느 시간대에 책을 읽는 편이세요?");

        // when
        memberAskService.updateMemberAsk(member.getId(), memberAskUpdateRequest);

        // then
        MemberAsk updatedMemberAsk = memberAskRepository.findByMember(member).orElseThrow();
        assertThat(updatedMemberAsk.getContents()).isEqualTo("어느 시간대에 책을 읽는 편이세요?");
    }

    @Test
    void 회원의_개인_질문_수정시_제한_길이를_넘어가면_예외가_발생한다() {
        // given
        Member member = memberRepository.save(Member.builder()
                .memberType(MemberType.APPLE)
                .oauthEmail("bookbla@bookbla.com")
                .build());
        MemberAsk memberAsk = MemberAsk.builder().member(member).contents("주로 어디서 책을 읽는 편이세요?")
                .build();
        memberAskRepository.save(memberAsk);

        MemberAskUpdateRequest memberAskUpdateRequest = new MemberAskUpdateRequest(" ".repeat(81));

        // when, then
        assertThatThrownBy(
                () -> memberAskService.updateMemberAsk(member.getId(), memberAskUpdateRequest))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("개인 질문은 공백 포함 80자 이하로만 작성가능합니다");
    }

}
