
package com.bookbla.americano.domain.member.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberAuthExceptionType implements ExceptionType {

    MEMBER_AUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "member_auth_001", "해당 식별자를 가진 회원 인가 정보가 존재하지 않습니다."),
    STATUS_IMAGE_ID_NOT_VALID(HttpStatus.BAD_REQUEST, "member_auth_002", "유효하지 학생증 체크 상태입니다.")
    ;


    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}