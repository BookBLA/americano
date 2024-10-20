package com.bookbla.americano.domain.quiz.controller.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class QuizQuestionVerifyResponse {

    private final Boolean isCorrect;

    public static QuizQuestionVerifyResponse of(Boolean isCorrect) {
        return QuizQuestionVerifyResponse.builder()
                .isCorrect(isCorrect)
                .build();
    }
}