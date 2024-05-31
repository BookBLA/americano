package com.bookbla.americano.domain.alarm.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PushAlarmAllCreateResponse {

    private final String title;
    private final String body;

    public static PushAlarmAllCreateResponse from(String title, String body) {
        return PushAlarmAllCreateResponse.builder()
            .title(title)
            .body(body)
            .build();
    }

}
