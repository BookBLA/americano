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
    private final String body;

    public static AlarmResponse from(ExpoPushTicket expoPushTicket) {
        return new ExpoAlarmResponse(
                expoPushTicket.getStatus() == Status.OK,
                expoPushTicket.getId(),
                expoPushTicket.getMessage()
        );
    }

    public static ExpoAlarmResponse fail() {
        return new ExpoAlarmResponse(
                false,
                "실패",
                "Expo 서버로부터 응답을 받는데 실패하였습니다"
        );
    }
}
