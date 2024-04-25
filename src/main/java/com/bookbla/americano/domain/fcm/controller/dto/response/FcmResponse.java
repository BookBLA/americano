package com.bookbla.americano.domain.fcm.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FcmResponse {

    private final String title;
    private final String contents;

}
