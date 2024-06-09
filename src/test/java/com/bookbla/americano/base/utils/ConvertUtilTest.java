package com.bookbla.americano.base.utils;

import java.util.Map;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ConvertUtilTest {

    @Test
    void 문자열을_맵으로_변환할_수_있다() {
        // given
        String expected = "이름: 이길여, 학교: 서울대학교, 전공: 의학과, 학번: 19510001";

        // when
        Map<String, String> actual = ConvertUtil.stringToMap(expected);

        // then
        assertAll(
                () -> assertThat(actual).containsEntry("이름", "이길여"),
                () -> assertThat(actual).containsEntry("학교", "서울대학교"),
                () -> assertThat(actual).containsEntry("전공", "의학과"),
                () -> assertThat(actual).containsEntry("학번", "19510001")
        );
    }

    @Test
    void 맵을_문자열로_변환할_수_있다() {
        // given
        Map<String, String> expected = Map.of(
                "이름", "이길여",
                "학교", "서울대학교",
                "전공", "의학과",
                "학번", "19510001"
        );

        // when
        String actual = ConvertUtil.mapToString(expected);

        // then
        assertAll(
                () -> assertThat(actual).contains("이름: 이길여"),
                () -> assertThat(actual).contains("학교: 서울대학교"),
                () -> assertThat(actual).contains("전공: 의학과"),
                () -> assertThat(actual).contains("학번: 19510001"),
                () -> assertThat(actual).contains(",")
        );
    }
}
