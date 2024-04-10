package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MailVerifyResponse {

    private String schoolEmail;
    private String emailVerifyStatus;

    public static MailVerifyResponse from(MemberAuth memberAuth) {
        return MailVerifyResponse.builder()
            .schoolEmail(memberAuth.getSchoolEmail())
            .emailVerifyStatus(memberAuth.getEmailVerifyStatus().name())
            .build();
    }

}
