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
class JustFriendTypeTest {

    @Test
    void 남사친_여사친_유형을_문자열_리스트로_받을_수_있다() {
        // when
        List<String> result = JustFriendType.getValues();

        // then
        assertThat(result).containsExactly("허용 X", "단 둘이 밥 먹기", "단 둘이 술 먹기", "단 둘이 여행 가기", "상관 없음");
    }

    @Test
    void 이름을_받아_남사친_여사친_유형을_찾을_수_있다() {
        // given
        String expected = "상관 없음";

        // when
        JustFriendType actual = JustFriendType.from(expected);

        // then
        assertThat(actual).isEqualTo(JustFriendType.EVERYTHING);
    }

    @ParameterizedTest(name = "유효하지_않은_남사친_여사친_유형이라면_예외가_발생한다")
    @ValueSource(strings = {"11111", "북블라최고", "swagg", "여하튼아닌것"})
    void 유효하지_않은_남사친_여사친_유형이라면_예외가_발생한다(String input) {
        // when, then
        assertThatThrownBy(() -> JustFriendType.from(input))
                .isInstanceOf(BaseException.class);
    }
}
