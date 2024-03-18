package com.bookbla.americano.domain.member.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberBookCreateResponse {

    private final Long memberBookId;

    public static MemberBookCreateResponse from(Long memberBookId) {
        return MemberBookCreateResponse.builder()
                .memberBookId(memberBookId)
                .build();
    }
}
