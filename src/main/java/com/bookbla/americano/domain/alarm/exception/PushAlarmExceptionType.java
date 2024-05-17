package com.bookbla.americano.domain.alarm.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PushAlarmExceptionType implements ExceptionType {

    NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "push_alarm_01", "존재하지 않은 토큰 코드입니다."),
    INVALID_EXPO_TOKEN(HttpStatus.BAD_REQUEST, "push_alarm_02", "유효하지 않는 EXPO 푸쉬 토큰입니다."),
    FAIL_TO_SEND_EXPO_SERVER(HttpStatus.BAD_REQUEST, "push_alarm_03", "EXPO 서버에 보내는데 실패하였습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
