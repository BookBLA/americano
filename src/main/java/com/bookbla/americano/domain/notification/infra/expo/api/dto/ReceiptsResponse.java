package com.bookbla.americano.domain.notification.infra.expo.api.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReceiptsResponse {

    private Map<String, ReceiptStatus> data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ReceiptStatus {

        private String status;
        private String message;

    }
}
