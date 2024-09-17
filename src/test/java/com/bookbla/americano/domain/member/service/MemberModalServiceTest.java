package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.response.MemberOnboardingStatusResponse;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MemberModalServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberModalService sut;

    @Test
    void 온보딩_상태를_변경할_수_있다() {
        // given
        Member member = memberRepository.save(Member.builder().build());
        // when
        MemberOnboardingStatusResponse response = sut.updateMemberOnboarding(member.getId(), "HOME");
        // then
        assertThat(response.getHomeOnboardingStatus()).isTrue();
        assertThat(response.getLibraryOnboardingStatus()).isFalse();
    }

    @Test
    void 유효하지_않은_온모딩_시_에러가_발생한다() {
        // given
        Member member = memberRepository.save(Member.builder().build());

        // when & then
        assertThatThrownBy(() -> sut.updateMemberOnboarding(member.getId(), "INVALID"))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("유효하지 않은 온보딩 상태입니다.");
    }
}