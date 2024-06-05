package com.bookbla.americano.domain.notification.infra.expo.dto;

import com.bookbla.americano.domain.notification.infra.expo.api.dto.ReceiptsResponse;
import com.bookbla.americano.domain.notification.service.dto.NotificationResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExpoNotificationResponse implements NotificationResponse {

    private final String status;
    private final String message;
    private final String error;

    public static NotificationResponse from(ReceiptsResponse.Data data) {
        return new ExpoNotificationResponse(
                data.getStatus(),
                data.getMessage(),
                data.getDetails().getError()
        );
    }
}
