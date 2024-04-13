package com.bookbla.americano.domain.aws.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UploadTypeNotValidException implements ExceptionType {

    UPLOAD_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "not_valid_image", "형식에 맞지 않는 업로드 타입입니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
