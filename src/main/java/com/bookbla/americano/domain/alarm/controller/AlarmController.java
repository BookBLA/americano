package com.bookbla.americano.domain.alarm.controller;

import com.bookbla.americano.domain.alarm.controller.dto.request.PushAlarmCreateRequest;
import com.bookbla.americano.domain.alarm.controller.dto.response.PushAlarmCreateResponse;
import com.bookbla.americano.domain.alarm.service.AlarmService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping("/sends")
    public ResponseEntity<PushAlarmCreateResponse> sendPushAlarm(
        @RequestBody @Valid PushAlarmCreateRequest pushAlarmCreateRequest
    ) {
        PushAlarmCreateResponse pushAlarmCreateResponse = alarmService.sendPushAlarm(pushAlarmCreateRequest);
        return ResponseEntity.ok(pushAlarmCreateResponse);
    }

}
