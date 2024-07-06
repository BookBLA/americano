package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberEmailExceptionType implements ExceptionType {

    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "member_email_001", "프로필이 등록되지 않은 회원입니다."),
    EMAIL_NOT_VALID(HttpStatus.BAD_REQUEST, "member_email_002", "형식에 맞지 않는 이메일입니다"),
    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "member_email_003", "이메일이 이미 존재합니다."),
    SEND_EMAIL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "member_email_004", "이메일 송신을 실패하였습니다."),
    NOT_EQUAL_VERIFY_CODE(HttpStatus.BAD_REQUEST, "member_email_005", "인증코드가 다릅니다."),
    NOT_EQUAL_SCHOOL_EMAIL(HttpStatus.BAD_REQUEST, "member_email_006", "이메일이 다릅니다."),
    EXPIRED_TIME(HttpStatus.BAD_REQUEST, "member_email_007", "인증 시간이 만료되었습니다."),
    MAIL_VERIFY_STATUS_NOT_VALID(HttpStatus.BAD_REQUEST, "member_email_008", "유효하지 않은 메일 검증 상태입니다."),
    EMAIL_NOT_REGISTERED(HttpStatus.BAD_REQUEST, "member_email_009", "이메일이 등록되지 않은 멤버입니다."),
    STILL_PENDING(HttpStatus.BAD_REQUEST, "member_email_010", "아직 이메일 인증 대기 상태입니다."),
    DOMAIN_URL_NOT_EQUAL(HttpStatus.BAD_REQUEST, "member_email_011", "학교 이메일의 도메인 URL이 해당 학교와 맞지 않습니다. "),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
