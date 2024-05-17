package com.bookbla.americano.domain.alarm.service;

import java.util.List;

import com.bookbla.americano.domain.alarm.service.dto.AlarmSendResponse;

public interface AlarmClient {

    List<AlarmSendResponse> send(String token, String title, String body);

}
