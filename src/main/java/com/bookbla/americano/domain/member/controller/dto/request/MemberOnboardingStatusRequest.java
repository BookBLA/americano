package com.bookbla.americano.domain.member.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class MemberOnboardingStatusRequest {

    @NotNull(message = "변경 할 상태를 입력하지 않았습니다.")
    private String onboarding;
}
