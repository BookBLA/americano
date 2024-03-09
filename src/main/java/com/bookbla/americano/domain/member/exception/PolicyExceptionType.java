package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PolicyExceptionType implements ExceptionType {

    EACH_POLICY_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "policy_001", "해당 약관이 존재하지 않습니다."),
    MEMBER_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "policy_002", "해당 회원에 대한 약관 정보들이 존재하지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
