package com.bookbla.americano.domain.alarm.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PushAlarmCreateRequest {

    private Long memberId;
    private String title;
    private String body;
}
