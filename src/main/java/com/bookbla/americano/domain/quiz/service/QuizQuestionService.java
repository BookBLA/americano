package com.bookbla.americano.domain.quiz.service;

import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionCreateRequest;

public interface QuizQuestionService {

    Long createQuizQuestion(Long memberId, Long memberBookId, QuizQuestionCreateRequest quizQuestionCreateRequest);

}
