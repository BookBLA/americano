package com.bookbla.americano.domain.alarm.infra.dto;

import com.bookbla.americano.domain.alarm.infra.api.dto.ReceiptsResponse;
import com.bookbla.americano.domain.alarm.service.dto.AlarmResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExpoAlarmResponse implements AlarmResponse {

    private final String status;
    private final String message;
    private final String error;

    public static AlarmResponse from(ReceiptsResponse.Data data) {
        return new ExpoAlarmResponse(
                data.getStatus(),
                data.getMessage(),
                data.getDetails().getError()
        );
    }
}
