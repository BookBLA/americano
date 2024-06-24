package com.bookbla.americano.domain.notification.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PushAlarmLogExceptionType implements ExceptionType {

    STATUS_NOT_VALID(HttpStatus.BAD_REQUEST, "push_alarm_log_01", "유효하지 않은 상태입니다."),
    TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "push_alarm_log_02", "유효하지 않은 타입입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
