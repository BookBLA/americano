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
class ContactTypeTest {

    @Test
    void 연락_스타일_유형을_문자열_리스트로_받을_수_있다() {
        // when
        List<String> result = ContactType.getValues();

        // then
        assertThat(result).containsExactly("느긋이", "칼답");
    }

    @Test
    void 해당_연락_유형을_찾을_수_있다() {
        // given
        String expected = "느긋이";

        // when
        ContactType actual = ContactType.from(expected);

        // then
        assertThat(actual).isEqualTo(ContactType.SLOW);
    }

    @ParameterizedTest(name = "유효하지_않은_연락_유형이라면_예외가_발생한다")
    @ValueSource(strings = {"cute", "kawaii", "swagg", "여하튼아닌것"})
    void 유효하지_않은_연락_유형이라면_예외가_발생한다(String input) {
        // when, then
        assertThatThrownBy(() -> ContactType.from(input))
                .isInstanceOf(BaseException.class);
    }
}
