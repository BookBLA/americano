package com.bookbla.americano.domain.notification.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PushAlarmSendAllEvent {

    private final String title;
    private final String contents;

}
