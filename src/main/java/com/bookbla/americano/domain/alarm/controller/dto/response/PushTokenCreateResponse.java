package com.bookbla.americano.domain.alarm.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PushTokenCreateResponse {

    private final Long memberId;
    private final String token;

    public static PushTokenCreateResponse from(Member member) {
        return PushTokenCreateResponse.builder()
            .memberId(member.getId())
            .token(member.getPushToken())
            .build();
    }
}
