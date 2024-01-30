package com.bookbla.americano.domain.member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.bookbla.americano.base.exception.BaseException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EmailTest {

    @Test
    void 형식에_맞지_않는_이메일은_생성시_예외가_발생한다() {
        // given
        String value = "boobkla@@@@";

        // when, then
        assertThatThrownBy(() -> new Email(value))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("형식에 맞지 않는 이메일입니다");
    }

    @Test
    void 값이_같으면_같은_이메일이다() {
        // given
        String value = "bookbla@bookbla.com";
        Email firstEmail = new Email(value);
        Email secondEmail = new Email(value);

        // when, then
        assertEquals(firstEmail, secondEmail);
    }
}
