package com.bookbla.americano.domain.notification.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.notification.controller.dto.response.MemberPushAlarmAllDeleteResponse;
import com.bookbla.americano.domain.notification.controller.dto.response.MemberPushAlarmDeleteResponse;
import com.bookbla.americano.domain.notification.controller.dto.response.MemberPushAlarmReadResponse;
import com.bookbla.americano.domain.notification.service.MemberPushAlarmService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "해당 회원의 푸시 알림 조회 API")
    @GetMapping
    public ResponseEntity<MemberPushAlarmReadResponse> readPushAlarm(
        @Parameter(hidden = true) @User LoginUser loginUser) {

        MemberPushAlarmReadResponse memberPushAlarmReadResponse = memberPushAlarmService.readPushAlarm(
            loginUser.getMemberId());
        return ResponseEntity.ok(memberPushAlarmReadResponse);
    }

    @Operation(summary = "해당 회원의 푸시 알림 1개 삭제 API")
    @DeleteMapping("/{memberPushAlarmId}")
    public ResponseEntity<MemberPushAlarmDeleteResponse> deletePushAlarm(
        @Parameter(hidden = true) @User LoginUser loginuser,
        @PathVariable("memberPushAlarmId") Long memberPushAlarmId) {

        MemberPushAlarmDeleteResponse memberPushAlarmDeleteResponse = memberPushAlarmService.deletePushAlarm(
            loginuser.getMemberId(), memberPushAlarmId
        );

        return ResponseEntity.ok(memberPushAlarmDeleteResponse);
    }

    @Operation(summary = "해당 회원의 푸시 알림 전체 삭제 API")
    @DeleteMapping
    public ResponseEntity<MemberPushAlarmAllDeleteResponse> deleteAllPushAlarm(
        @Parameter(hidden = true) @User LoginUser loginUser) {

        MemberPushAlarmAllDeleteResponse memberPushAlarmAllDeleteResponse = memberPushAlarmService.deleteAllPushAlarm(
            loginUser.getMemberId());

        return ResponseEntity.ok(memberPushAlarmAllDeleteResponse);
    }

}
