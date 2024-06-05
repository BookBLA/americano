package com.bookbla.americano.domain.notification.service;

import java.util.List;

import com.bookbla.americano.domain.notification.service.dto.NotificationResponse;

public interface NotificationClient {

    List<NotificationResponse> send(String token, String title, String body);

    List<NotificationResponse> sendAll(List<String> tokens, String title, String body);

}
