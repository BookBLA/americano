package com.bookbla.americano.domain.admin.controller;

import java.util.List;

import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberNotificationRequest;
import com.bookbla.americano.domain.admin.service.AdminMarketingService;
import com.bookbla.americano.domain.notification.service.dto.NotificationResponse;
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
    public ResponseEntity<List<NotificationResponse>> sendNotifications(
            @RequestBody @Valid AdminMemberNotificationRequest request
    ) {
        List<NotificationResponse> response = adminMarketingService.sendNotifications(request.toDto());
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "개인 푸시 알림 전송 API")
    @PostMapping("/notification")
    public ResponseEntity<List<NotificationResponse>> sendNotification(
            @RequestBody @Valid AdminMemberNotificationRequest request
    ) {
        List<NotificationResponse> response = adminMarketingService.sendNotification(request.toDto(), request.getMemberId());
        return ResponseEntity.ok(response);
    }

}
