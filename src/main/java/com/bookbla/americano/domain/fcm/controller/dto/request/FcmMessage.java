package com.bookbla.americano.domain.fcm.controller.dto.request;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class FcmMessage {
    private boolean validateOnly;
    private Message message;

    @Builder
    @Getter
    public static class Message {
        private Notification notification;
        private String token;
    }

    @Builder
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}