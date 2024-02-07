package com.bookbla.americano.domain.member.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DateStyleTest {

    @Test
    void 데이트_스타일_목록을_문자열로_받을_수_있다() {
        // when
        List<String> result = DateStyle.getValues();

        // then
        assertThat(result).containsExactly("집 데이트", "야외 데이트");
    }

}
