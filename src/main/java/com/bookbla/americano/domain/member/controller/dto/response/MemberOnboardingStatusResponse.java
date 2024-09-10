package com.bookbla.americano.domain.member.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberOnboardingStatusResponse {

    private Boolean onboarding;

    public static MemberOnboardingStatusResponse from(Boolean onboarding) {
        return MemberOnboardingStatusResponse.builder()
                .onboarding(onboarding)
                .build();
    }
}
