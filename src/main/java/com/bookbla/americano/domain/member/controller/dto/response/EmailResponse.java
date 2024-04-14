package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberEmail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EmailResponse {
    private String schoolEmail;
    private String emailVerifyStatus;

    public static EmailResponse from(MemberEmail memberEmail) {
        return EmailResponse.builder()
            .schoolEmail(memberEmail.getSchoolEmail())
            .emailVerifyStatus(memberEmail.getEmailVerifyStatus().name())
            .build();
    }
}

