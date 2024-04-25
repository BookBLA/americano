package com.bookbla.americano.domain.fcm.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FcmTokenCreateResponse {

    private Long memberId;
    private String fcmToken;

    public static FcmTokenCreateResponse from(Member member) {
        return FcmTokenCreateResponse.builder()
            .memberId(member.getId())
            .fcmToken(member.getFcmToken())
            .build();
    }
}
