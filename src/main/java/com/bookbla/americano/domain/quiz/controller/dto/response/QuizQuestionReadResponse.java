package com.bookbla.americano.domain.quiz.controller.dto.response;

import com.bookbla.americano.domain.quiz.QuizQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class QuizQuestionReadResponse {

        private Long id;
        private String quiz;
        private String firstChoice;
        private String secondChoice;
        private String thirdChoice;

        public static QuizQuestionReadResponse from(QuizQuestion quizQuestion) {
            return QuizQuestionReadResponse.builder()
                .id(quizQuestion.getId())
                .quiz(quizQuestion.getContents())
                .firstChoice(quizQuestion.getFirstChoice())
                .secondChoice(quizQuestion.getSecondChoice())
                .thirdChoice(quizQuestion.getThirdChoice())
                .build();
        }
}
