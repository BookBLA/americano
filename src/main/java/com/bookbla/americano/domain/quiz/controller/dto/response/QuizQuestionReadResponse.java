package com.bookbla.americano.domain.quiz.controller.dto.response;

import com.bookbla.americano.domain.quiz.repository.entity.QuizQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

        public static QuizQuestionReadResponse fromShuffledChoices(QuizQuestion quizQuestion) {
                List<String> choices = Arrays.asList(
                        quizQuestion.getFirstChoice(),
                        quizQuestion.getSecondChoice(),
                        quizQuestion.getThirdChoice()
                );

                Collections.shuffle(choices);

                return QuizQuestionReadResponse.builder()
                        .id(quizQuestion.getId())
                        .quiz(quizQuestion.getContents())
                        .firstChoice(choices.get(0))
                        .secondChoice(choices.get(1))
                        .thirdChoice(choices.get(2))
                        .build();
        }
}
