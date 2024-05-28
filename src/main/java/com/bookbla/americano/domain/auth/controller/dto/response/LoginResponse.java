package com.bookbla.americano.domain.auth.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginResponse {
    
    private final String accessToken;
    private final Long memberId;
    private final String memberStatus;

    public static LoginResponse of(String accessToken, Member member) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .memberId(member.getId())
                .memberStatus(member.getMemberStatus().name())
                .build();
    }
}
