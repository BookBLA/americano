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
    private String oauthProfileImageUrl;
    private String memberType;
    private String memberStatus;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
            .id(member.getId())
            .oauthEmail(member.getOauthEmail())
            .oauthProfileImageUrl(member.getOauthProfileImageUrl())
            .memberType(member.getMemberType().name())
            .memberStatus(member.getMemberStatus().name())
            .build();
    }

}
