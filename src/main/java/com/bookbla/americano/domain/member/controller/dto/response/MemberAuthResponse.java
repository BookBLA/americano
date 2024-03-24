package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberAuthResponse {

    private Long memberId;
    private Long memberAuthId;
    private String schoolEmail;
    private String emailVerifyStatus;

    public static MemberAuthResponse from(Member member, MemberAuth memberAuth) {
        return MemberAuthResponse.builder()
            .memberId(member.getId())
            .memberAuthId(memberAuth.getId())
            .schoolEmail(memberAuth.getSchoolEmail())
            .emailVerifyStatus(memberAuth.getEmailVerifyStatus().name())
            .build();
    }

}
