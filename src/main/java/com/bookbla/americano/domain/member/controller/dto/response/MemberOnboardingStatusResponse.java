package com.bookbla.americano.domain.member.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberOnboardingStatusResponse {

    private Boolean homeOnboardingStatus;
    private Boolean libraryOnboardingStatus;

    public static MemberOnboardingStatusResponse from(Boolean home, Boolean library) {
        return MemberOnboardingStatusResponse.builder()
                .homeOnboardingStatus(home)
                .libraryOnboardingStatus(library)
                .build();
    }
}
