package com.bookbla.americano.domain.sendbird.exception;

import lombok.Getter;
import org.sendbird.client.ApiException;

@Getter
public class SendbirdException extends RuntimeException {

    public SendbirdException(ApiException apiException) {
        super(apiException.getMessage(), apiException);
    }
}