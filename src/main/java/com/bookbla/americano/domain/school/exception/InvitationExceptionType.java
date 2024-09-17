package com.bookbla.americano.domain.school.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum InvitationExceptionType implements ExceptionType {

    INVITATION_EXISTS(HttpStatus.BAD_REQUEST, "invitation-001", "이미 초대코드를 입력하셨습니다"),
    INVALID_INVITATION_CODE_MYSELF(HttpStatus.BAD_REQUEST, "invitation-002", "본인의 초대코드와 일치합니다");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
