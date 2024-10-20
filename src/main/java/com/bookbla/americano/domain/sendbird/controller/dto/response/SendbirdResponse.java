package com.bookbla.americano.domain.sendbird.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SendbirdResponse {
    private final Long memberId;
    private final String sendbirdToken;

    public static SendbirdResponse of(Member member, String sendbirdToken) {
        return SendbirdResponse.builder()
                .memberId(member.getId())
                .sendbirdToken(sendbirdToken)
                .build();
    }
}
