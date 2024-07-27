package com.bookbla.americano.domain.admin.controller.docs;

import java.util.List;

import com.bookbla.americano.domain.admin.controller.dto.request.AdminMemberNotificationRequest;
import com.bookbla.americano.domain.admin.controller.dto.request.AdminNotificationRequest;
import com.bookbla.americano.domain.notification.service.dto.NotificationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "관리자 마케팅", description = "관리자 마케팅용 API 모음입니다.")
public interface AdminMarketingControllerDocs {

    @Operation(summary = "광고 동의 회원 대상 푸시 알림 전송 API")
    @ApiResponse(
            responseCode = "200",
            description = "광고 동의한 모든 회원에게 푸시 알림을 전송합니다"
    )
    @PostMapping
    ResponseEntity<Void> sendNotifications(@RequestBody @Valid AdminNotificationRequest request);


    @Operation(summary = "광고 동의 회원 대상 푸시 알림 전송 API(deprecated)")
    @ApiResponse(
            responseCode = "200",
            description = "광고 동의한 모든 회원에게 푸시 알림을 전송합니다"
    )
    @PostMapping
    ResponseEntity<Void> sendAlarms(@RequestBody @Valid AdminNotificationRequest request);

    @Operation(summary = "개인 푸시 알림 전송 API")
    @ApiResponse(
            responseCode = "200",
            description = "광고 동의한 개인 회원에게 푸시 알림을 전송합니다"
    )
    @PostMapping
    ResponseEntity<List<NotificationResponse>> sendNotification(@RequestBody @Valid AdminMemberNotificationRequest request);

}
