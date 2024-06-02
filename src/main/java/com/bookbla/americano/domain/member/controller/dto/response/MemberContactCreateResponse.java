package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberContactCreateResponse {

    private Long memberId;

    public static MemberContactCreateResponse from(Member member) {
        return MemberContactCreateResponse.builder()
            .memberId(member.getId())
            .build();
    }
}
