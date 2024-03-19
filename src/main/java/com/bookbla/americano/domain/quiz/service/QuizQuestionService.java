package com.bookbla.americano.domain.quiz.service;

import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionCreateRequest;
import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionUpdateRequest;

public interface QuizQuestionService {

    Long createQuizQuestion(Long memberId, Long memberBookId, QuizQuestionCreateRequest quizQuestionCreateRequest);

    void updateQuizQuestion(Long memberId, Long memberBookId, QuizQuestionUpdateRequest quizQuestionUpdateRequest);

}
