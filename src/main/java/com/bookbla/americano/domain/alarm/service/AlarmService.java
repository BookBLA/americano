package com.bookbla.americano.domain.alarm.service;

import com.bookbla.americano.domain.alarm.controller.dto.request.PushAlarmCreateRequest;
import com.bookbla.americano.domain.alarm.controller.dto.request.PushTokenCreateRequest;
import com.bookbla.americano.domain.alarm.controller.dto.response.PushAlarmCreateResponse;
import com.bookbla.americano.domain.alarm.controller.dto.response.PushTokenCreateResponse;
import io.github.jav.exposerversdk.PushClientException;

public interface AlarmService {

    PushAlarmCreateResponse sendPushAlarm(PushAlarmCreateRequest pushAlarmCreateRequest) throws PushClientException;

    PushTokenCreateResponse createPushToken(Long memberId, PushTokenCreateRequest pushTokenCreateRequest);
}
