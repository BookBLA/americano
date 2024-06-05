package com.bookbla.americano.domain.alarm.service;

import java.util.List;

import com.bookbla.americano.domain.alarm.service.dto.AlarmResponse;

public interface AlarmClient {

    List<AlarmResponse> send(String token, String title, String body);

    List<AlarmResponse> sendAll(List<String> tokens, String title, String body);

}
