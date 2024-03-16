package com.bookbla.americano.domain.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.bookbla.americano.domain.member.controller.dto.response.StylesResponse;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StyleServiceTest {

    @Autowired
    private StyleService styleService;

    @Test
    void 스타일_목록을_확인할_수_있다() {
        // when
        StylesResponse stylesResponse = styleService.readStyles();

        // then
        assertAll(
                () -> assertThat(stylesResponse.getSmokeTypes()).containsExactly("흡연", "비흡연", "가끔"),
                () -> assertThat(stylesResponse.getDrinkTypes()).containsExactly("안마심", "월 1~2회",
                        "주 1회", "주 1회 이상", "매일"),
                () -> assertThat(stylesResponse.getContactTypes()).containsExactly("느긋이", "칼답"),
                () -> assertThat(stylesResponse.getDateStyleTypes()).containsExactly("집 데이트",
                        "야외 데이트"),
                () -> assertThat(stylesResponse.getDateCostTypes()).containsExactly("더치페이",
                        "번갈아가면서 사기", "여유 있는 사람이 좀 더", "데이트 통장"),
                () -> assertThat(stylesResponse.getJustFriendTypes()).containsExactly("허용 X",
                        "단둘이 밥 먹기", "단둘이 술 먹기", "단둘이 여행 가기", "상관 없음")
        );
    }

}