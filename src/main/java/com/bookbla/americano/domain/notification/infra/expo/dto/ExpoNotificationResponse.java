package com.bookbla.americano.domain.notification.infra.expo.dto;

import com.bookbla.americano.domain.notification.infra.expo.api.dto.ReceiptsResponse;
import com.bookbla.americano.domain.notification.service.dto.NotificationResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ExpoNotificationResponse implements NotificationResponse {

    private final String id;
    private final String status;
    private final String message;

    public static NotificationResponse from(String id, ReceiptsResponse.ReceiptStatus receiptStatus) {
        return new ExpoNotificationResponse(
                id,
                receiptStatus.getStatus(),
                receiptStatus.getMessage()
        );
    }

    @Override
    public boolean isSuccess() {
        return id.equals("ok");
    }
}
