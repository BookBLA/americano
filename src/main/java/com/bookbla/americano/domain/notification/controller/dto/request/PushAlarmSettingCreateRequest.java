package com.bookbla.americano.domain.notification.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PushAlarmSettingCreateRequest {
    private Boolean pushAlarmEnabled;
}
