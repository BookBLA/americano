package com.bookbla.americano.domain.member.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DrinkTypeTest {

    @Test
    void 음주_유형을_문자열_리스트로_받을_수_있다() {
        // when
        List<String> result = DrinkType.getValues();

        // then
        assertThat(result).containsExactly("안마심", "월 1~2회", "주 1회", "주 1회 이상", "매일");
    }
}
