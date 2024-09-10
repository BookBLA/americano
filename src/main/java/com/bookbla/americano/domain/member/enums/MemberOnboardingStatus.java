package com.bookbla.americano.domain.member.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberOnboardingStatus {

    UNSEEN("u", "미확인"),
    SEEN("s", "확인");

    private final String value;
    private final String description;
}
