package com.bookbla.americano.domain.alarm.service;

import com.bookbla.americano.domain.member.repository.entity.Member;

public interface AlarmService {

    void sendPushAlarm(Member member, String title, String body);
}
