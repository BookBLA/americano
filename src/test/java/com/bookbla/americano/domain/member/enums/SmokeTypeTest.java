package com.bookbla.americano.domain.member.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bookbla.americano.base.exception.BaseException;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SmokeTypeTest {

    @Test
    void 흡연_유형을_문자열_리스트로_받을_수_있다() {
        // when
        List<String> result = SmokeType.getValues();

        // then
        assertThat(result).containsExactly("흡연", "비흡연", "가끔");
    }

    @Test
    void 이름을_받아_흡연_유형을_찾을_수_있다() {
        // given
        String expected = "흡연";

        // when
        SmokeType actual = SmokeType.from(expected);

        // then
        assertThat(actual).isEqualTo(SmokeType.SMOKE);
    }

    @ParameterizedTest(name = "유효하지_않은_흡연_유형이라면_예외가_발생한다")
    @ValueSource(strings = {"11111", "북블라최고", "swagg", "여하튼아닌것"})
    void 유효하지_않은_흡연_유형이라면_예외가_발생한다(String input) {
        // when, then
        assertThatThrownBy(() -> SmokeType.from(input))
                .isInstanceOf(BaseException.class);
    }

}
