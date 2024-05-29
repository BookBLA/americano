package com.bookbla.americano.domain.alarm.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum MemberPushAlarmExceptionType implements ExceptionType {

    ID_NOT_FOUND(HttpStatus.NOT_FOUND, "member_push_alaram-001", "해당 id가 존재하지 않습니다."),
    ALARAM_MEMBER_MISMATCH(HttpStatus.BAD_REQUEST, "member_push_alarm-002", "해당 알람의 주인이 아닙니다.");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
