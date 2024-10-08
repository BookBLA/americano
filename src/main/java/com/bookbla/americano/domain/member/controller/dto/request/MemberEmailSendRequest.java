package com.bookbla.americano.domain.member.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberEmailSendRequest {

    @NotNull(message = "학교 이름이 입력되지 않았습니다.")
    private String schoolName;

    @NotNull(message = "학교 이메일이 입력되지 않았습니다.")
    private String schoolEmail;

}

