package com.bookbla.americano.domain.admin.excpetion;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AdminExceptionType implements ExceptionType {

    FAIL_LOGIN_TRIAL(HttpStatus.BAD_REQUEST, "admin-001", "최대 비밀번호 입력 횟수를 초과했습니다."),
    SESSION_OVER(HttpStatus.UNAUTHORIZED, "admin-002", "세션이 만료되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
