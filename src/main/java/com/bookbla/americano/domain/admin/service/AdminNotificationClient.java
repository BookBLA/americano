package com.bookbla.americano.domain.admin.service;

public interface AdminNotificationClient {

    void send(String title, String contents);
}
