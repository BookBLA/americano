package com.bookbla.americano.domain.quiz.controller.dto.request;

import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.quiz.QuizQuestion;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QuizQuestionCreateRequest {

    @NotBlank(message = "리뷰가 입력되지 않았습니다.")
    private String review;

    @NotBlank(message = "퀴즈가 입력되지 않았습니다.")
    private String quiz;

    @NotBlank(message = "퀴즈 정답이 입력되지 않았습니다.")
    private String quizAnswer;

    @NotBlank(message = "퀴즈 답안이 입력되지 않았습니다.")
    private String firstWrongChoice;

    @NotBlank(message = "퀴즈 답안이 입력되지 않았습니다.")
    private String secondWrongChoice;

    public QuizQuestion toQuizQuestionWith(MemberBook memberBook) {
        return QuizQuestion.builder()
                .memberBook(memberBook)
                .contents(quiz)
                .firstChoice(quizAnswer)
                .secondChoice(firstWrongChoice)
                .thirdChoice(secondWrongChoice)
                .build();
    }
}
