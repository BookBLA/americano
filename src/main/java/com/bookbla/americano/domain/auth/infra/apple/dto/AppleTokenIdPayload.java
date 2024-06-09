package com.bookbla.americano.domain.auth.infra.apple.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AppleTokenIdPayload {

    private String sub;
    private String email;

}
