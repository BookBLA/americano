package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberExceptionType implements ExceptionType {

    EMAIL_NOT_VALID(HttpStatus.BAD_REQUEST, "4001", "형식에 맞지 않는 이메일입니다"),
    MEMBER_TYPE_NOT_VALID(HttpStatus.NOT_FOUND, "4003", "존재하지 않는 유저 타입입니다~~"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
