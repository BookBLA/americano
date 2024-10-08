package com.bookbla.americano.base.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.stream.Collectors;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.base.exception.ExceptionType;
import com.bookbla.americano.base.log.LogLevel;
import com.bookbla.americano.base.log.discord.DiscordAlarm;
import com.bookbla.americano.base.response.ExceptionResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.sendbird.client.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DELIMITER = ", ";

    @DiscordAlarm(level = LogLevel.WARN)
    @ExceptionHandler(BaseException.class)
    ResponseEntity<ExceptionResponse> handleBaseException(HttpServletRequest request,
                                                          BaseException e) {
        ExceptionType exceptionType = e.exceptionType();
        log.warn("{} URI: {}", exceptionType.getMessage(), request.getRequestURI());
        return ResponseEntity.status(exceptionType.getHttpStatus())
                .body(new ExceptionResponse(exceptionType));
    }

    @DiscordAlarm(level = LogLevel.ERROR)
    @ExceptionHandler(Exception.class)
    ResponseEntity<ExceptionResponse> handleException(HttpServletRequest request, Exception e) {
        log.error("예상하지 못한 예외가 발생했습니다. URI: {}, 내용: {}", request.getRequestURI(), convertToString(e));
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(BaseExceptionType.UNKNOWN_SERVER_ERROR));
    }

    private String convertToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    @DiscordAlarm(level = LogLevel.WARN)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        String requestURI = ((ServletWebRequest) request).getRequest().getRequestURI();
        String message = e.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(DELIMITER));
        log.warn("잘못된 요청이 들어왔습니다. URI: {}, 내용 : {}", requestURI, message);
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse(BaseExceptionType.ARGUMENT_NOT_VALID, message));
    }

    // TODO: 코드 추가 수정하기
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleApiException(ApiException e) {
        String errorMessage = "SendBird API 오류: " + e.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        log.error("SendBird API 오류", e);

        return ResponseEntity.status(status).body(errorMessage);
    }
}
