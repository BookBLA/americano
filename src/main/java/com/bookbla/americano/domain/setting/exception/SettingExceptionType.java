package com.bookbla.americano.domain.setting.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SettingExceptionType implements ExceptionType {

    SETTING_NOT_FOUND(HttpStatus.BAD_REQUEST, "setting_001", "등록된 설정 정보가 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
