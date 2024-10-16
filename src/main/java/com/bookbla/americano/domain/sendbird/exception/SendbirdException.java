package com.bookbla.americano.domain.sendbird.exception;

import lombok.Getter;
import org.sendbird.client.ApiException;

@Getter
public class SendbirdException extends RuntimeException {

    private final int code;
    private final String message;

    public SendbirdException(ApiException e) {
        this.code = e.getCode();
        this.message = e.getMessage();
    }
}