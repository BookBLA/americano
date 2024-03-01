package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MailVerifyResponse {

    private Long id;
    private String schoolEmail;
    private String mailVerifyStatus;

    public static MailVerifyResponse from(MemberAuth memberAuth) {
        return MailVerifyResponse.builder()
            .id(memberAuth.getId())
            .schoolEmail(memberAuth.getSchoolEmail())
            .mailVerifyStatus(memberAuth.getMailVerifyStatus().name())
            .build();
    }

}
