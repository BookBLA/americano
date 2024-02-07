package com.bookbla.americano.domain.member.enums;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JustFriendTypeTest {

    @Test
    void 남사친_여사친_유형을_문자열_리스트로_받을_수_있다() {
        // when
        List<String> result = JustFriendType.getValues();

        // then
        assertThat(result).containsExactly("허용 X", "단 둘이 밥 먹기", "단 둘이 술 먹기", "단 둘이 여행 가기", "상관 없음");
    }
}
