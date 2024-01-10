package com.bookbla.americano.base.handler;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.base.exception.ExceptionType;
import com.bookbla.americano.base.response.ExceptionResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BaseException.class)
    ResponseEntity<ExceptionResponse> handleException(HttpServletRequest request, BaseException e) {
        ExceptionType type = e.exceptionType();
        log.warn("잘못된 요청이 들어왔습니다. URI: {},  내용: {}", request.getRequestURI(), type.getStackTrace());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(type));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionResponse> handleException(HttpServletRequest request, Exception e) {
        log.error("예상하지 못한 예외가 발생했습니다. URI: {}, 내용: {}", request.getRequestURI(), e.getMessage());
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(BaseExceptionType.INTERNAL_SERVER_ERROR));
    }
}
