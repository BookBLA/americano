package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {

    private Long id;
    private String oauthEmail;
    private String memberType;
    private String memberStatus;
    private String memberGender;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
            .id(member.getId())
            .oauthEmail(member.getOauthEmail())
            .memberType(member.getMemberType().name())
            .memberStatus(member.getMemberStatus().name())
            .memberGender(member.getMemberProfile().getGender().name())
            .build();
    }

}
