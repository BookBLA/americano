package com.bookbla.americano.domain.postcard.controller.dto.response;

import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostcardStatusResponse {

    private final PostcardStatus postcardStatus;

    public static PostcardStatusResponse from(PostcardStatus postcardStatus) {
        return new PostcardStatusResponse(postcardStatus);
    }
}
