package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberStatusResponse {

    private String memberStatus;

    public static MemberStatusResponse from(Member member) {
        return MemberStatusResponse.builder()
            .memberStatus(member.getMemberStatus().getValue())
            .build();
    }

}
