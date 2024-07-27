package com.bookbla.americano.domain.admin.controller;

import java.util.List;

import com.bookbla.americano.domain.admin.controller.docs.AdminMarketingControllerDocs;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberNotificationRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminNotificationRequest;
import com.bookbla.americano.domain.admin.service.AdminMarketingService;
import com.bookbla.americano.domain.notification.service.dto.NotificationResponse;
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
public class AdminMarketingController implements AdminMarketingControllerDocs {

    private final AdminMarketingService adminMarketingService;

    @PostMapping("/notifications")
    public ResponseEntity<Void> sendNotifications(
            @RequestBody @Valid AdminNotificationRequest request
    ) {
        adminMarketingService.sendNotifications(request.toDto());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/notification")
    public ResponseEntity<List<NotificationResponse>> sendNotification(
            @RequestBody @Valid AdminMemberNotificationRequest request
    ) {
        List<NotificationResponse> response = adminMarketingService.sendNotification(request.toDto(), request.getMemberId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/alarm")
    public ResponseEntity<Void> sendAlarms(
            @RequestBody @Valid AdminNotificationRequest request
    ) {
        adminMarketingService.sendNotifications(request.toDto());
        return ResponseEntity.ok().build();
    }
}
