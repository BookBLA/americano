package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MailExceptionType implements ExceptionType {

    EMAIL_NOT_VALID(HttpStatus.BAD_REQUEST, "mail_001", "형식에 맞지 않는 이메일입니다"),
    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "mail_002", "이메일이 이미 존재합니다."),
    SEND_EMAIL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "mail_003", "이메일 송신을 실패하였습니다."),
    NOT_EQUAL_VERIFY_CODE(HttpStatus.BAD_REQUEST, "mail_004", "인증코드가 다릅니다."),
    EXPIRED_TIME(HttpStatus.BAD_REQUEST, "mail_005", "인증 시간이 만료되었습니다."),
    MAIL_VERIFY_STATUS_NOT_VALID(HttpStatus.BAD_REQUEST, "mail_006", "유효하지 않음 메일 검증 상태입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
