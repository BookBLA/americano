package com.bookbla.americano.domain.member.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DateCostTypeTest {

    @Test
    void 데이트비_유형을_문자열_리스트로_받을_수_있다() {
        // when
        List<String> result = DateCostType.getValues();

        // then
        assertThat(result).containsExactly("더치페이", "번갈아가면서 사기", "여유있는 사람이 좀 더", "데이트 통장");
    }

}
