package com.bookbla.americano.domain.payment.controller;

import com.bookbla.americano.domain.payment.controller.dto.request.AppleNotificationRequest;
import com.bookbla.americano.domain.payment.service.ApplePaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/payments")
@RestController
public class PaymentSubscriber {

    private final ApplePaymentService applePaymentService;

    @PostMapping("/in-app/apple/notification")
    public ResponseEntity<Void> receiveAppleNotification(
            @RequestBody AppleNotificationRequest request
    ) {
        applePaymentService.receiveAppleNotification(request.getSignedPayload());
        return ResponseEntity.ok().build();
    }
}
