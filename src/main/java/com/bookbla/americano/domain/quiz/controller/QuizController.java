package com.bookbla.americano.domain.quiz.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionVerifyRequest;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionReadResponse;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionVerifyResponse;
import com.bookbla.americano.domain.quiz.service.QuizQuestionService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizQuestionService quizQuestionService;

    @GetMapping("/{memberBookId}")
    public ResponseEntity<QuizQuestionReadResponse> createQuizQuestion(
            @Parameter(hidden = true) @User LoginUser loginUser, @PathVariable Long memberBookId
    ) {
        return ResponseEntity.ok(quizQuestionService.getQuizQuestion(loginUser.getMemberId(), memberBookId));
    }

    @PostMapping("/verify")
    public ResponseEntity<QuizQuestionVerifyResponse> verifyQuizQuestion(
            @RequestBody QuizQuestionVerifyRequest quizQuestionVerifyRequest
    ) {
        return ResponseEntity.ok(quizQuestionService.verifyQuizQuestion(quizQuestionVerifyRequest));
    }
}
