package com.bookbla.americano.domain.quiz.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionCreateRequest;
import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionUpdateRequest;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionReadResponse;
import com.bookbla.americano.domain.quiz.service.QuizQuestionService;
import io.swagger.v3.oas.annotations.Parameter;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizQuestionService quizQuestionService;

    // Todo: 리팩터링
    @PostMapping("/{memberBookId}")
    public ResponseEntity<Void> createQuizQuestion(
        @Parameter(hidden = true) @User LoginUser loginUser, @PathVariable Long memberBookId,
        @RequestBody @Valid QuizQuestionCreateRequest quizQuestionCreateRequest
    ) {
        Long quizQuestionId = quizQuestionService.createQuizQuestion(loginUser.getMemberId(),
                memberBookId, quizQuestionCreateRequest);
        return ResponseEntity
                .created(URI.create("/quizzes/" + quizQuestionId.toString()))
                .build();
    }

    @GetMapping("/{memberBookId}")
    public ResponseEntity<QuizQuestionReadResponse> readQuizQuestion(
        @Parameter(hidden = true) @User LoginUser loginUser, @PathVariable Long memberBookId
    ) {
        QuizQuestionReadResponse quizQuestionReadResponse = quizQuestionService.getQuizQuestion(
                loginUser.getMemberId(), memberBookId);
        return ResponseEntity.ok(quizQuestionReadResponse);
    }

    @PutMapping("/{memberBookId}")
    public ResponseEntity<Void> updateQuizQuestion(
        @Parameter(hidden = true) @User LoginUser loginUser, @PathVariable Long memberBookId,
        @RequestBody @Valid QuizQuestionUpdateRequest quizQuestionUpdateRequest
    ) {
        quizQuestionService.updateQuizQuestion(loginUser.getMemberId(), memberBookId,
                quizQuestionUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}
