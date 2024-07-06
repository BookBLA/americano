package com.bookbla.americano.domain.school.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@Getter
public enum SchoolExceptionType implements ExceptionType {

    EMAIL_DOMAIN_NOT_FOUND(HttpStatus.NOT_FOUND, "school-001", "존재하지 않는 이메일 도메인입니다"),
    SCHOOL_NOT_OPEN(HttpStatus.NOT_FOUND, "school-002", "아직 서비스 오픈되지 않은 학교입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
