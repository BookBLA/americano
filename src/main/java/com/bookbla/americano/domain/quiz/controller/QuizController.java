package com.bookbla.americano.domain.quiz.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionReadResponse;
import com.bookbla.americano.domain.quiz.service.QuizQuestionService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizQuestionService quizQuestionService;

    @GetMapping("/{memberBookId}")
    public ResponseEntity<QuizQuestionReadResponse> createQuizQuestion(
            @Parameter(hidden = true) @User LoginUser loginUser, @PathVariable Long memberBookId
    ) {
        QuizQuestionReadResponse quizQuestionReadResponse = quizQuestionService.getQuizQuestion(loginUser.getMemberId(), memberBookId);
        return ResponseEntity.ok(quizQuestionReadResponse);
    }

}
