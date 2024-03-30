package com.bookbla.americano.domain.test.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TestSignUpResponse {

    private final String accessToken;
    private final Long memberId;

    public static TestSignUpResponse of(Member member, String token) {
        return new TestSignUpResponse(token, member.getId());
    }

}
