package com.bookbla.americano.domain.alarm.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.alarm.controller.dto.response.MemberPushAlarmResponse;
import com.bookbla.americano.domain.alarm.service.MemberPushAlarmService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members/push-alarms")
@RequiredArgsConstructor
public class MemberPushAlarmController {

    private final MemberPushAlarmService memberPushAlarmService;

    @GetMapping
    public ResponseEntity<MemberPushAlarmResponse> readPushAlarm(
        @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        MemberPushAlarmResponse memberPushAlarmResponse = memberPushAlarmService.readPushAlarm(loginUser.getMemberId());
        return ResponseEntity.ok(memberPushAlarmResponse);
    }

    @DeleteMapping("/{memberPushAlarmId}")
    public ResponseEntity<Void> deletePushAlarm(
        @Parameter(hidden = true) @User LoginUser loginuser,
        @PathVariable("memberPushAlarmId") Long memberPushAlarmId
    ) {
        memberPushAlarmService.deletePushAlarm(loginuser.getMemberId(), memberPushAlarmId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllPushAlarm(
        @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        memberPushAlarmService.deleteAllPushAlarm(loginUser.getMemberId());
        return ResponseEntity.noContent().build();
    }

}
