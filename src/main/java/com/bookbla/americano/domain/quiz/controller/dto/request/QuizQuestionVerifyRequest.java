package com.bookbla.americano.domain.quiz.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuizQuestionVerifyRequest {

    private Long quizId;
    private String quizAnswer;
}
