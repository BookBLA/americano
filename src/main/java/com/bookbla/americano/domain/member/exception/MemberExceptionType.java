package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberExceptionType implements ExceptionType {

    EMAIL_NOT_VALID(HttpStatus.BAD_REQUEST, "member_001", "형식에 맞지 않는 이메일입니다"),
    MEMBER_TYPE_NOT_VALID(HttpStatus.NOT_FOUND, "member_002", "존재하지 않는 유저 타입입니다"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "member_003", "해당 식별자를 가진 회원이 존재하지 않습니다."),
    STYLE_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "member_004", "스타일이 등록되지 않은 회원입니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
