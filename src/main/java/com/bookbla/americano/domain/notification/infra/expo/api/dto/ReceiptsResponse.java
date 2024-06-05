package com.bookbla.americano.domain.notification.infra.expo.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReceiptsResponse {

    private List<Data> data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class Data {

        private String status;
        private String message;
        private Details details;
        private String id;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        public static class Details {

            private String error;
        }
    }
}
