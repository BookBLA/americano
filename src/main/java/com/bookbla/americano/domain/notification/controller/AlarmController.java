package com.bookbla.americano.domain.notification.controller;

import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmAllCreateRequest;
import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmCreateRequest;
import com.bookbla.americano.domain.notification.controller.dto.response.PushAlarmAllCreateResponse;
import com.bookbla.americano.domain.notification.controller.dto.response.PushAlarmCreateResponse;
import com.bookbla.americano.domain.notification.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "한 명에게 푸시 알림 전송 API")
    @PostMapping("/sends")
    public ResponseEntity<PushAlarmCreateResponse> sendPushAlarm(
        @RequestBody @Valid PushAlarmCreateRequest pushAlarmCreateRequest
    ) {
        PushAlarmCreateResponse pushAlarmCreateResponse = alarmService.sendPushAlarm(
            pushAlarmCreateRequest);
        return ResponseEntity.ok(pushAlarmCreateResponse);
    }

    @Operation(summary = "전체 유저에게 푸시 알림 전송 API")
    @PostMapping("/sends/all")
    public ResponseEntity<PushAlarmAllCreateResponse> sendAllPushAlarm(
        @RequestBody @Valid PushAlarmAllCreateRequest pushAlarmAllCreateRequest
    ) {
        PushAlarmAllCreateResponse pushAlarmAllCreateResponse = alarmService.sendPushAlarmAll(
            pushAlarmAllCreateRequest);
        return ResponseEntity.ok(pushAlarmAllCreateResponse);
    }


}
