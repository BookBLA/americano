package com.bookbla.americano.domain.test.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TestLoginResponse {

    private final String accessToken;

    public static TestLoginResponse of(String token) {
        return new TestLoginResponse(token);
    }
}
