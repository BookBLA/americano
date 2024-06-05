package com.bookbla.americano.domain.notification.service;

import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmAllCreateRequest;
import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmCreateRequest;
import com.bookbla.americano.domain.notification.controller.dto.response.PushAlarmAllCreateResponse;
import com.bookbla.americano.domain.notification.controller.dto.response.PushAlarmCreateResponse;
import com.bookbla.americano.domain.member.repository.entity.Member;

public interface AlarmService {

    PushAlarmCreateResponse sendPushAlarm(PushAlarmCreateRequest pushAlarmCreateRequest);

    void sendPushAlarm(Member member, String title, String body);

    PushAlarmAllCreateResponse sendPushAlarmAll(PushAlarmAllCreateRequest pushAlarmAllCreateRequest);
}
