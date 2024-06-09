package com.bookbla.americano.domain.quiz.controller.dto.response;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.quiz.repository.entity.QuizQuestion;
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
    private String review;

    public static QuizQuestionReadResponse from(QuizQuestion quizQuestion, MemberBook memberBook) {
        return QuizQuestionReadResponse.builder()
                .id(quizQuestion.getId())
                .quiz(quizQuestion.getContents())
                .firstChoice(quizQuestion.getFirstChoice())
                .secondChoice(quizQuestion.getSecondChoice())
                .thirdChoice(quizQuestion.getThirdChoice())
                .review(memberBook.getReview())
                .build();
    }

    public static QuizQuestionReadResponse fromShuffledChoices(QuizQuestion quizQuestion, MemberBook memberBook) {
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
                .review(memberBook.getReview())
                .build();
    }
}
