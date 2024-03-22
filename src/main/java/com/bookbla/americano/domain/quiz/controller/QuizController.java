package com.bookbla.americano.domain.quiz.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionCreateRequest;
import com.bookbla.americano.domain.quiz.controller.dto.request.QuizQuestionUpdateRequest;
import com.bookbla.americano.domain.quiz.controller.dto.response.QuizQuestionReadResponse;
import com.bookbla.americano.domain.quiz.service.QuizQuestionService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/quizzes")
public class QuizController {

    private final QuizQuestionService quizQuestionService;

    @PostMapping("/{memberBookId}")
    public ResponseEntity<Void> createQuizQuestion(
            @LoginUser Long memberId, @PathVariable Long memberBookId,
            @RequestBody @Valid QuizQuestionCreateRequest quizQuestionCreateRequest
    ) {
        Long quizQuestionId = quizQuestionService.createQuizQuestion(memberId, memberBookId, quizQuestionCreateRequest);
        return ResponseEntity
                .created(URI.create("/quizzes/" + quizQuestionId.toString()))
                .build();
    }

    @GetMapping("/{memberBookId}")
    public ResponseEntity<QuizQuestionReadResponse> createQuizQuestion(
            @LoginUser Long memberId, @PathVariable Long memberBookId
    ) {
        QuizQuestionReadResponse quizQuestionReadResponse = quizQuestionService.getQuizQuestion(memberId, memberBookId);
        return ResponseEntity.ok(quizQuestionReadResponse);
    }

    @PutMapping("/{memberBookId}")
    public ResponseEntity<Void> createQuizQuestion(
            @LoginUser Long memberId, @PathVariable Long memberBookId,
            @RequestBody @Valid QuizQuestionUpdateRequest quizQuestionUpdateRequest
    ) {
        quizQuestionService.updateQuizQuestion(memberId, memberBookId, quizQuestionUpdateRequest);
        return ResponseEntity.noContent().build();
    }

}
