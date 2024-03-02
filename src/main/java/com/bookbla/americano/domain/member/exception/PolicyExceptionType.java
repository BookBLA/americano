package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PolicyExceptionType implements ExceptionType {

    NOT_EQUAL_POLICY_COUNT(HttpStatus.BAD_REQUEST, "policy_001", "선택 약관 개수가 맞지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
