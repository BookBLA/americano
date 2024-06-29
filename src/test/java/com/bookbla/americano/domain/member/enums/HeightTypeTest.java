package com.bookbla.americano.domain.member.enums;

import com.bookbla.americano.base.exception.BaseException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HeightTypeTest {

    @Test
    void 해당하는_키_범위를_찾을_수_있다() {
        // given
        String expected = "160cm 이상~165cm 미만";

        // when
        HeightType actual = HeightType.from(expected);

        // then
        assertThat(actual).isEqualTo(HeightType.OVER_160_AND_LESS_THAN_165);
    }

    @ParameterizedTest(name = "유효하지_않은_키_범위라면_예외가_발생한다")
    @ValueSource(strings = {"cute", "kawaii", "abcd", "-1"})
    void 유효하지_않은_키_범위라면_예외가_발생한다(String input) {
        // when, then
        assertThatThrownBy(() -> HeightType.from(input))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining("유효하지 않은 키 범위입니다.");
    }
}
