package com.bookbla.americano.domain.notification.service;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PostcardAlarmEvent {

    private final Member sendMember;
    private final Member receiveMember;

}
