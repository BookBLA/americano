package com.bookbla.americano.domain.quiz.service;

import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionCreateRequest;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionCreateResponse;

public interface QuizQuestionService {

    QuizQuestionCreateResponse createQuizQuestion(Long memberId, Long memberBookId, QuizQuestionCreateRequest quizQuestionCreateRequest);

}
