package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberVerifyExceptionType implements ExceptionType {

    ID_NOT_FOUND(HttpStatus.NOT_FOUND, "member_verify-001", "존재하지 않는 식별자입니다"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
