package com.bookbla.americano.domain.member;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.bookbla.americano.base.exception.BaseException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class EmailTest {

    @Test
    void 형식에_맞지_않는_이메일은_예외가_발생한다() {
        // given
        String value = "boobkla@@@@";

        // when & then
        assertThatThrownBy(() -> new Email(value))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("형식에 맞지 않는 이메일입니다");
    }
}
