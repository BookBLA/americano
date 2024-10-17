package com.bookbla.americano.domain.notification.event;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PostcardAlarmEvent {

    private final Member targetMember;
    private final Member postcardAcceptMember;

}
