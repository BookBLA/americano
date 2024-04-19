package com.bookbla.americano.domain.auth.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LoginExceptionType implements ExceptionType {

    CANNOT_LOGIN_MEMBER_DELETED(HttpStatus.BAD_REQUEST, "login_001", "회원이 탈퇴인 상태이므로 로그인할 수 없습니다."),
    NOT_THIRTY_DAYS_AFTER(HttpStatus.BAD_REQUEST, "login_002", "회원탈퇴한 뒤에 30일이 지나지 않았습니다."),
    NOT_MEMBER_DELETED(HttpStatus.BAD_REQUEST, "login_003", "회원 탈퇴 상태가 아닙니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
