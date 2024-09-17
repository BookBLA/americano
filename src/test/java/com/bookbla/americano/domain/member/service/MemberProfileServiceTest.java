package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.controller.dto.response.MemberNameVerifyResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class MemberProfileServiceTest {

    @Autowired
    private MemberProfileService sut;

    @Test
    void 닉네임을_검증할_수_있다() {
        // given
        String name = "unique_nickname";

        // when
        MemberNameVerifyResponse memberNameVerifyResponse = sut.verifyMemberName(name);

        // then
        assertThat(memberNameVerifyResponse.isVerified()).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"북블라", "시발"})
    void 금지어가_들어가면_생성할_수_없다(String name) {
        // when & then
        assertThatThrownBy(() -> sut.verifyMemberName(name))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("사용할 수 없는 단어가(비속어, 북블라 등) 포함되어있습니다.");
    }
}