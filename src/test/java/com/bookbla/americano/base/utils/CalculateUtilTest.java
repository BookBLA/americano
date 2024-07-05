package com.bookbla.americano.base.utils;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CalculateUtilTest {

    @Test
    void 백분위를_계산할_수_있다() {
        // given
        int numerator = 5;
        int denominator = 30;

        // when
        int actual = CalculateUtil.calculatePercentage(numerator, denominator);

        // then
        assertThat(actual).isEqualTo(16);
    }

    @Test
    void 분모가_0이면_계산할_수_없다() {
        // given
        int numerator = 10;
        int denominator = 0;

        // when, then
        assertThatThrownBy(() -> CalculateUtil.calculatePercentage(numerator, denominator))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
