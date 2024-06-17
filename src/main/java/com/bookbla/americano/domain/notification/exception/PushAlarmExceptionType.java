package com.bookbla.americano.domain.notification.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PushAlarmExceptionType implements ExceptionType {

    NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "push_alarm_01", "존재하지 않은 토큰 코드입니다."),
    INVALID_EXPO_TOKEN(HttpStatus.BAD_REQUEST, "push_alarm_02", "유효하지 않는 EXPO 푸쉬 토큰입니다."),
    FAIL_TO_SEND_EXPO_SERVER(HttpStatus.BAD_REQUEST, "push_alarm_03", "EXPO 서버에 보내는데 실패하였습니다."),
    INVALID_PUSH_CLIENT(HttpStatus.BAD_REQUEST, "push_alarm_04", "Push Client 생성 에러입니다."),
    INVALID_MEMBER_STATUS(HttpStatus.BAD_REQUEST, "push_alarm_05", "유효하지 않은 멤버 상태입니다. [COMPLETED or MATCHING_DISABLED]"),
    INVALID_OWNER(HttpStatus.BAD_REQUEST, "push_alarm-06", "해당 알림의 소유자가 아닙니다"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
