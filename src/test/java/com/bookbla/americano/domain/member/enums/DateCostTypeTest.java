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
class DateCostTypeTest {

    @Test
    void 데이트비_유형을_문자열_리스트로_받을_수_있다() {
        // when
        List<String> result = DateCostType.getValues();

        // then
        assertThat(result).containsExactly("더치페이", "번갈아가면서 사기", "여유 있는 사람이 좀 더", "데이트 통장");
    }

    @Test
    void 이름을_받아_데이트비_유형을_찾을_수_있다() {
        // given
        String expected = "데이트 통장";

        // when
        DateCostType actual = DateCostType.from(expected);

        // then
        assertThat(actual).isEqualTo(DateCostType.DATE_ACCOUNT);
    }

    @ParameterizedTest(name = "유효하지_않은_데이트비_유형이라면_예외가_발생한다")
    @ValueSource(strings = {"cute", "kawaii", "swagg", "여하튼아닌것"})
    void 유효하지_않은_데이트비_유형이라면_예외가_발생한다(String input) {
        // when, then
        assertThatThrownBy(() -> DateCostType.from(input))
                .isInstanceOf(BaseException.class);
    }

}
