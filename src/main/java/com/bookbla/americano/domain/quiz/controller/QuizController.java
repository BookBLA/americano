package com.bookbla.americano.domain.quiz.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionVerifyRequest;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionReadResponse;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionVerifyResponse;
import com.bookbla.americano.domain.quiz.service.QuizQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name="독서 퀴즈" , description = "독서 퀴즈 조회 및 검증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizQuestionService quizQuestionService;

    @Operation(summary = "독서 퀴즈 조회 API")
    @GetMapping("/{memberBookId}")
    public ResponseEntity<QuizQuestionReadResponse> createQuizQuestion(
            @Parameter(hidden = true) @User LoginUser loginUser, @PathVariable Long memberBookId
    ) {
        return ResponseEntity.ok(quizQuestionService.getQuizQuestion(loginUser.getMemberId(), memberBookId));
    }

    @Operation(summary = "독서 퀴즈 검증 API")
    @PostMapping("/verify")
    public ResponseEntity<QuizQuestionVerifyResponse> verifyQuizQuestion(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid QuizQuestionVerifyRequest quizQuestionVerifyRequest
    ) {
        return ResponseEntity.ok(quizQuestionService.verifyQuizQuestion(loginUser.getMemberId(), quizQuestionVerifyRequest));
    }
}
