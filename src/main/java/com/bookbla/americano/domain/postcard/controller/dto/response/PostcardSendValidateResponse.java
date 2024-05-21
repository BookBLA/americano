package com.bookbla.americano.domain.postcard.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PostcardSendValidateResponse {

    private final boolean isRefused;

    public static PostcardSendValidateResponse from(boolean isRefused) {
        return new PostcardSendValidateResponse(isRefused);
    }

}
