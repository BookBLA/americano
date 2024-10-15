package com.bookbla.americano.domain.auth.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
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
    private final String sendbirdToken;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String deletedAt;


    public static LoginResponse of(String accessToken, Member member) {
        LoginResponse.LoginResponseBuilder builder = LoginResponse.builder()
            .accessToken(accessToken)
            .memberId(member.getId())
            .memberStatus(member.getMemberStatus().name())
            .sendbirdToken(member.getSendbirdToken());

        if ("DELETED".equals(member.getMemberStatus().name())) {
            builder.deletedAt(member.getDeleteAt().toString());
        }

        return builder.build();
    }
}
