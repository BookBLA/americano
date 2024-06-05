package com.bookbla.americano.domain.alarm.infra.dto;

import com.bookbla.americano.domain.alarm.service.dto.AlarmResponse;
import io.github.jav.exposerversdk.ExpoPushTicket;
import io.github.jav.exposerversdk.enums.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExpoAlarmResponse implements AlarmResponse {

    private final boolean isSuccess;
    private final String id;
    private final String message;
    private final String token;

    public static AlarmResponse from(ExpoPushTicket expoPushTicket) {
        return new ExpoAlarmResponse(
                expoPushTicket.getStatus() == Status.OK,
                expoPushTicket.getId(),
                expoPushTicket.getMessage(),
                "토큰 없음"
        );
    }

    public static ExpoAlarmResponse of(ExpoPushTicket expoPushTicket, String token) {
        return new ExpoAlarmResponse(
                expoPushTicket.getStatus() == Status.OK,
                expoPushTicket.getId(),
                expoPushTicket.getMessage(),
                token
        );
    }
}
