package com.bookbla.americano.domain.test.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TestSignUpResponse {

    private final String accessToken;
    private final Long memberSignUpInformationId;

    public static TestSignUpResponse of(Long id, String token) {
        return new TestSignUpResponse(token, id);
    }

}
