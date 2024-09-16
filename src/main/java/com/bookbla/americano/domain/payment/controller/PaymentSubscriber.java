package com.bookbla.americano.domain.payment.controller;

import com.bookbla.americano.domain.payment.controller.dto.request.AppleNotificationRequest;
import com.bookbla.americano.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PaymentSubscriber {

    private final PaymentService paymentService;

    @PostMapping("/in-app/apple/notification")
    public ResponseEntity<Void> receiveAppleNotification(
            @RequestBody AppleNotificationRequest request
    ) {
        paymentService.receiveAppleNotification(request.getSignedPayload());
        return ResponseEntity.ok().build();
    }
}
