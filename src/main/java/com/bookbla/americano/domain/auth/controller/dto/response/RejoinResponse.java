package com.bookbla.americano.domain.auth.controller.dto.response;

import com.bookbla.americano.domain.auth.controller.dto.request.RejoinRequest;
import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RejoinResponse {

    private final String accessToken;
    private final Long memberId;

    public static RejoinResponse from(String accessToken, Member member) {
        return RejoinResponse.builder()
            .accessToken(accessToken)
            .memberId(member.getId())
            .build();
    }

}
