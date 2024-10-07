package com.bookbla.americano.domain.postcard.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostcardReadResponse {
    private final String channelUrl;

    public static PostcardReadResponse from(String channelUrl) {
        return new PostcardReadResponse(channelUrl);
    }
}
