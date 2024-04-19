package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberDeleteResponse {

    private Long id;
    private String memberStatus;
    private String deleteAt;

    public static MemberDeleteResponse from(Member member) {
        return MemberDeleteResponse.builder()
            .id(member.getId())
            .memberStatus(member.getMemberStatus().name())
            .deleteAt(member.getDeleteAt().toString())
            .build();
    }
}
