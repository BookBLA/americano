package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.response.MemberPushAlarmResponse;
import com.bookbla.americano.domain.member.service.MemberPushAlarmService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members/push-alarms")
@RequiredArgsConstructor
public class MemberPushAlarmController {

    private final MemberPushAlarmService memberPushAlarmService;

    @GetMapping
    public ResponseEntity<MemberPushAlarmResponse> readPushAlarm(
        @Parameter(hidden = true) @User LoginUser loginUser) {

        MemberPushAlarmResponse memberPushAlarmResponse = memberPushAlarmService.readPushAlarm(
            loginUser.getMemberId());
        return ResponseEntity.ok(memberPushAlarmResponse);
    }

}
