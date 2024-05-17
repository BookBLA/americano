package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberTokenCreateResponse {

    private final Long memberId;
    private final String token;

    public static MemberTokenCreateResponse from(Member member) {
        return MemberTokenCreateResponse.builder()
            .memberId(member.getId())
            .token(member.getPushToken())
            .build();
    }
}
