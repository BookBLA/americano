package com.bookbla.americano.domain.admin.controller;

import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberNotificationRequest;
import com.bookbla.americano.domain.admin.service.AdminMarketingService;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RequiredArgsConstructor
@RestController
public class AdminMarketingController {

    private final AdminMarketingService adminMarketingService;

    @Operation(summary = "광고 동의 회원 대상 푸시 알림 전송 API")
    @PostMapping("/alarm")
    public ResponseEntity<Void> sendNotifications(
            @RequestBody @Valid AdminMemberNotificationRequest request
    ) {
        adminMarketingService.sendNotifications(request.toDto());
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "개인 푸시 알림 전송 API")
    @PostMapping("/notification")
    public ResponseEntity<Void> sendNotification(
            @RequestBody @Valid AdminMemberNotificationRequest request
    ) {
        adminMarketingService.sendNotification(request.toDto(), request.getMemberId());
        return ResponseEntity.ok().build();
    }

}
