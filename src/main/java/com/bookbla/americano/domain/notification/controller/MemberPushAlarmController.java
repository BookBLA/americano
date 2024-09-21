package com.bookbla.americano.domain.notification.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.notification.controller.dto.response.MemberPushAlarmReadResponses;
import com.bookbla.americano.domain.notification.controller.dto.response.PushAlarmSettingResponse;
import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmSettingCreateRequest;
import com.bookbla.americano.domain.notification.controller.dto.response.MemberPushAlarmResponse;
import com.bookbla.americano.domain.notification.service.MemberPushAlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members/push-alarms")
@RequiredArgsConstructor
public class MemberPushAlarmController {

    private final MemberPushAlarmService memberPushAlarmService;

    @Operation(summary = "해당 회원의 푸시 알림 조회 API")
    @GetMapping
    public ResponseEntity<MemberPushAlarmResponse> readPushAlarm(
        @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberPushAlarmResponse memberPushAlarmResponse = memberPushAlarmService.readPushAlarm(
            loginUser.getMemberId());
        return ResponseEntity.ok(memberPushAlarmResponse);
    }

    @Operation(summary = "해당 회원의 푸시 알림 조회 API")
    @GetMapping
    public ResponseEntity<MemberPushAlarmReadResponses> readPushAlarmPageable(
        @Parameter(hidden = true) @User LoginUser loginUser,
        Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        MemberPushAlarmReadResponses readResponses = memberPushAlarmService.readPushAlarms(
            loginUser.getMemberId(), pageRequest);
        return ResponseEntity.ok(readResponses);
    }


    @Operation(summary = "해당 회원의 푸시 알림 1개 삭제 API")
    @DeleteMapping("/{memberPushAlarmId}")
    public ResponseEntity<Void> deletePushAlarm(
        @Parameter(hidden = true) @User LoginUser loginuser,
        @PathVariable("memberPushAlarmId") Long memberPushAlarmId
    ) {
        memberPushAlarmService.deletePushAlarm(loginuser.getMemberId(), memberPushAlarmId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "해당 회원의 푸시 알림 전체 삭제 API")
    @DeleteMapping
    public ResponseEntity<Void> deleteAllPushAlarm(
        @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        memberPushAlarmService.deleteAllPushAlarm(loginUser.getMemberId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "해당 회원의 푸시 알림 설정 수정 API")
    @PostMapping("/settings")
    public ResponseEntity<PushAlarmSettingResponse> updatePushAlarmSetting(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @RequestBody @Valid PushAlarmSettingCreateRequest request
    ) {
        PushAlarmSettingResponse response = memberPushAlarmService.updatePushAlarmSetting(
            loginUser.getMemberId(), request);
        return ResponseEntity.ok(response) ;
    }

    @Operation(summary = "해당 회원의 푸시 알림 설정 조회 API")
    @GetMapping("/settings")
    public ResponseEntity<PushAlarmSettingResponse> getPushAlarmSetting(
        @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        PushAlarmSettingResponse response = memberPushAlarmService.getPushAlarmSetting(
            loginUser.getMemberId());
        return ResponseEntity.ok(response) ;
    }

}
