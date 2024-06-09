package com.bookbla.americano.domain.postcard.controller.dto.response;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostcardSendValidateResponse {

    private final boolean isRefused;

    public static PostcardSendValidateResponse from(boolean isRefused) {
        return new PostcardSendValidateResponse(isRefused);
    }

    public boolean getIsRefused() {
        return isRefused;
    }
}
