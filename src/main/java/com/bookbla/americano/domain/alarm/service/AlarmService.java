package com.bookbla.americano.domain.alarm.service;

import com.bookbla.americano.domain.alarm.controller.dto.request.PushAlarmCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberTokenCreateRequest;
import com.bookbla.americano.domain.alarm.controller.dto.response.PushAlarmCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberTokenCreateResponse;
import com.bookbla.americano.domain.member.repository.entity.Member;
import io.github.jav.exposerversdk.PushClientException;
import org.springframework.transaction.annotation.Transactional;

public interface AlarmService {

    PushAlarmCreateResponse sendPushAlarm(PushAlarmCreateRequest pushAlarmCreateRequest)
        throws PushClientException;

    @Transactional
    void sendPushAlarm(Member member, String title, String body);
}
