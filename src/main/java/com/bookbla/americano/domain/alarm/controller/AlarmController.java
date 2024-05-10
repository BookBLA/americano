package com.bookbla.americano.domain.alarm.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.alarm.controller.dto.request.PushAlarmCreateRequest;
import com.bookbla.americano.domain.alarm.controller.dto.request.PushTokenCreateRequest;
import com.bookbla.americano.domain.alarm.controller.dto.response.PushAlarmCreateResponse;
import com.bookbla.americano.domain.alarm.controller.dto.response.PushTokenCreateResponse;
import com.bookbla.americano.domain.alarm.service.AlarmService;
import io.github.jav.exposerversdk.PushClientException;
import io.swagger.v3.oas.annotations.Parameter;
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

    @PostMapping("/tokens")
    public ResponseEntity<PushTokenCreateResponse> createPushToken(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid PushTokenCreateRequest pushTokenCreateRequest
    ) {
        PushTokenCreateResponse pushTokenCreateResponse = alarmService.createPushToken(
            loginUser.getMemberId(), pushTokenCreateRequest);

        return ResponseEntity.ok(pushTokenCreateResponse);
    }

    @PostMapping("/sends")
    public ResponseEntity<PushAlarmCreateResponse> sendPushAlarm(
        @RequestBody @Valid PushAlarmCreateRequest pushAlarmCreateRequest
    ) throws PushClientException {

        PushAlarmCreateResponse pushAlarmCreateResponse = alarmService.sendPushAlarm(
            pushAlarmCreateRequest);
        return ResponseEntity.ok(pushAlarmCreateResponse);
    }

}
