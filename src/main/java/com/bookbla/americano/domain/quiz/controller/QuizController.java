package com.bookbla.americano.domain.quiz.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionCreateRequest;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionCreateResponse;
import com.bookbla.americano.domain.quiz.service.QuizQuestionService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizQuestionService quizQuestionService;

    @PostMapping("/{memberBookId}")
    public ResponseEntity<QuizQuestionCreateResponse> createQuizQuestion(
            @LoginUser Long memberId, @PathVariable Long memberBookId,
            @RequestBody @Valid QuizQuestionCreateRequest quizQuestionCreateRequest
    ) {
        QuizQuestionCreateResponse quizQuestionCreateResponse = quizQuestionService.createQuizQuestion(
                memberId, memberBookId, quizQuestionCreateRequest);
        return ResponseEntity
                .created(URI.create("/quizzes/" + quizQuestionCreateResponse.getQuizQuestionId()))
                .body(quizQuestionCreateResponse);
    }

}
