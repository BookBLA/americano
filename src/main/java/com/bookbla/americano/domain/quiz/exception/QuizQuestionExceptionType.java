package com.bookbla.americano.domain.quiz.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QuizQuestionExceptionType implements ExceptionType {

    MEMBER_QUIZ_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QuizQuestion-001", "해당 회원의 독서 퀴즈가 등록되지 않았습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
