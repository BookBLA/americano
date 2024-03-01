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
    private String phoneNumeber;
    private String studentIdImageUrl;

    public static MemberAuthResponse from(Member member, MemberAuth memberAuth) {
        return new MemberAuthResponse(
            member.getId(),
            memberAuth.getId(),
            memberAuth.getSchoolEmail(),
            memberAuth.getPhoneNumber(),
            memberAuth.getStudentIdImageUrl()
        );
    }

}
