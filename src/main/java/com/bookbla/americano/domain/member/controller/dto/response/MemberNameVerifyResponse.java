package com.bookbla.americano.domain.member.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberNameVerifyResponse {

    private boolean isVerified;

    public static MemberNameVerifyResponse of(boolean isVerified) {
        return new MemberNameVerifyResponse(isVerified);
    }
}
