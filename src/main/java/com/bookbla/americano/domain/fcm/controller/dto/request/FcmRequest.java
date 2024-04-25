package com.bookbla.americano.domain.fcm.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class FcmRequest {
    private String targetToken;
    private String title;
    private String body;
}
