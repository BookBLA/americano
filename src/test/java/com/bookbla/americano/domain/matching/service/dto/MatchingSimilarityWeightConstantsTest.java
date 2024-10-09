package com.bookbla.americano.domain.matching.service.dto;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.bookbla.americano.domain.matching.service.dto.MatchingSimilarityWeightConstants.getReviewWeight;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MatchingSimilarityWeightConstantsTest {

    @Test
    public void 리뷰_글자수에_따라_정확한_가중치가_나와야한다() {
        assertAll(
                // 리뷰 길이가 100 이상일 경우
                () -> assertEquals(1.0, getReviewWeight(100), 0.0001),
                () -> assertEquals(1.0, getReviewWeight(150), 0.0001),

                // 95 ~ 99
                () -> assertEquals(0.95, getReviewWeight(95), 0.0001),

                // 90 ~ 94
                () -> assertEquals(0.9, getReviewWeight(94), 0.0001),

                // 85 ~ 89
                () -> assertEquals(0.85, getReviewWeight(88), 0.0001),

                // 75 ~ 79
                () -> assertEquals(0.75, getReviewWeight(77), 0.0001),

                // 55 ~ 59
                () -> assertEquals(0.55, getReviewWeight(56), 0.0001),

                // 25 ~ 29
                () -> assertEquals(0.25, getReviewWeight(25), 0.0001),

                // 10 ~ 14
                () -> assertEquals(0.1, getReviewWeight(12), 0.0001),

                // 0 ~ 4
                () -> assertEquals(0.05, getReviewWeight(0), 0.0001),
                () -> assertEquals(0.05, getReviewWeight(4), 0.0001)
        );
    }

    @Test
    public void 리뷰_길이가_5_이하일_경우_최소_가중치_반환() {
        assertAll(
                () -> assertEquals(0.05, getReviewWeight(1), 0.0001),
                () -> assertEquals(0.05, getReviewWeight(4), 0.0001),
                () -> assertEquals(0.05, getReviewWeight(0), 0.0001)
        );
    }

    @Test
    public void 리뷰_길이가_100_이상일_경우_최대_가중치_반환() {
        assertAll(
                () -> assertEquals(1.0, getReviewWeight(100), 0.0001),
                () -> assertEquals(1.0, getReviewWeight(150), 0.0001)
        );
    }
}
