package com.bookbla.americano.base.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorType implements ExceptionType {
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "Invalid credentials", "Invalid email or password"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "Password mismatch", "Passwords do not match"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "This token is unsupported", "Invalid token"),
    INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "잘못된 JWT 서명입니다.", "Invalid token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Token has expired", "Invalid token"),
    MISSING_AUTH_HEADER(HttpStatus.UNAUTHORIZED, "Missing authorization header", "Invalid token"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "Unauthorized access to the requested resource", "Unauthorized access"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access to the requested resource is denied", "Access denied"),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    public static AuthErrorType findByType(String type) {
        try {
            return AuthErrorType.valueOf(type);
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }
}
