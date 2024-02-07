package com.bookbla.americano.domain.member.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

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
}
