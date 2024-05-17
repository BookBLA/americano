package com.bookbla.americano.domain.alarm.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AlarmSendResponse {

    private final String success;
    private final String title;
    private final String body;
    private final String token;

}
