package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberExceptionType implements ExceptionType {

    EMAIL_NOT_VALID("1111", "형식에 맞지 않는 이메일입니다", "asd".repeat(3)),
    MEMBER_TYPE_NOT_VALID("4003", "존재하지 않는 유저 타입입니다~~", "asd".repeat(3)),
    ;

    private final String errorCode;
    private final String message;
    private final String stackTrace;

}
