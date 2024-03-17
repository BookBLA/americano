package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MailResendResponse {

    private String schoolEmail;

    public static MailResendResponse from(MemberAuth memberAuth) {
        return MailResendResponse.builder()
            .schoolEmail(memberAuth.getSchoolEmail())
            .build();
    }

}
