package com.bookbla.americano.domain.aws.exception;

import com.bookbla.americano.base.exception.ExceptionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AwsException implements ExceptionType {

    UPLOAD_TYPE_NOT_VALID(HttpStatus.BAD_REQUEST, "not_valid_image", "형식에 맞지 않는 업로드 타입입니다."),
    AWS_COMMUNICATION_ERROR(HttpStatus.BAD_GATEWAY, "aws_communication_error", "업로드에 실패하였습니다. 다시 시도해주세요."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
