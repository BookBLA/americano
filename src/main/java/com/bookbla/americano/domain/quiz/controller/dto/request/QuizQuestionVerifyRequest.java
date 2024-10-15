package com.bookbla.americano.domain.quiz.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class QuizQuestionVerifyRequest {

    @NotNull(message = "퀴즈 출제자의 아이디가 입력되지 않았습니다.")
    private Long quizMakerId;

    @NotNull(message = "퀴즈 아이디가 입력되지 않았습니다.")
    private Long quizId;

    @NotNull(message = "퀴즈 정답이 입력되지 않았습니다.")
    private String quizAnswer;
}
