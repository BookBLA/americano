package com.bookbla.americano.domain.auth.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AppleExceptionType implements ExceptionType {

    TOKEN_ERROR(HttpStatus.BAD_REQUEST, "apple-001", "애플 로그인 중 예외가 발생했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
