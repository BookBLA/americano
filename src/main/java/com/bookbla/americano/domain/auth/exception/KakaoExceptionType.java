package com.bookbla.americano.domain.auth.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum KakaoExceptionType implements ExceptionType {

    TOKEN_ERROR(HttpStatus.BAD_REQUEST, "kakao_001", "토큰 코드 에러 발생"),
    MEMBER_RESOURCE_ERROR(HttpStatus.BAD_REQUEST, "kakao_002", "리소스 에러 발생"),
    ;
    
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

}
