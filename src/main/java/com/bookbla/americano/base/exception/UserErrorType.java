package com.bookbla.americano.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorType implements ExceptionType {

    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User not found", "User not found"),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "Duplicate email", "Email already exists"),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "Duplicate nickname", "Nickname already exists"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid password", "Invalid password"),
    INVALID_EMAIL_FORMAT(HttpStatus.BAD_REQUEST, "Invalid email format", "Invalid email format"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    public static UserErrorType findByType(String type) {
        try {
            return UserErrorType.valueOf(type);
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }
}
