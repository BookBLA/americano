package com.bookbla.americano.domain.fcm.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FcmTokenDeleteResponse {

    private Long memberId;

    public static FcmTokenDeleteResponse from(Member member) {
        return FcmTokenDeleteResponse.builder()
            .memberId(member.getId())
            .build();
    }
}
