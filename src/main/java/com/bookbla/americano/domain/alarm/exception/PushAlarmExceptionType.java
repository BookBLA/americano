package com.bookbla.americano.domain.alarm.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PushAlarmExceptionType implements ExceptionType {

    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "push_alarm_01", "존재하지 않은 토큰 코드입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
