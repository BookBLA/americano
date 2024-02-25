package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MailExceptionType implements ExceptionType {

    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "4004", "이메일이 이미 존재합니다."),
    SEND_EMAIL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "5001", "이메일 송신을 실패하였습니다."),
    NOT_EQUAL_VERIFY_CODE(HttpStatus.BAD_REQUEST, "4005", "인증코드가 다릅니다."),
    EXPIRED_TIME(HttpStatus.BAD_REQUEST, "4006", "인증 시간이 만료되었습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
