package com.bookbla.americano.domain.quiz.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizQuestionUpdateRequest {

    @NotBlank(message = "퀴즈가 입력되지 않았습니다.")
    private String quiz;

    @NotBlank(message = "퀴즈 정답이 입력되지 않았습니다.")
    private String quizAnswer;

    @NotBlank(message = "퀴즈 답안이 입력되지 않았습니다.")
    private String firstWrongChoice;

    @NotBlank(message = "퀴즈 답안이 입력되지 않았습니다.")
    private String secondWrongChoice;

}
