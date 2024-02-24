package com.bookbla.americano.domain.member.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bookbla.americano.base.exception.BaseException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MbtiTest {

    @ParameterizedTest(name = "해당하는_mbti_유형을_찾을_수_있다")
    @ValueSource(strings = {"intp", "INTP"})
    void 해당하는_mbti유형을_찾을_수_있다(String input) {
        // when
        Mbti actual = Mbti.from(input);

        // then
        assertThat(actual).isEqualTo(Mbti.INTP);
    }

    @ParameterizedTest(name = "유효하지_않은_MBTI_유형이라면_예외가_발생한다")
    @ValueSource(strings = {"cute", "kawaii", "swagg", "여하튼MBTI아닌것"})
    void 유효하지_않은_MBTI_유형이라면_예외가_발생한다(String input) {
        // when, then
        assertThatThrownBy(() -> Mbti.from(input))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("유효하지 않은 MBTI입니다");
    }
}
