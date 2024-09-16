package com.bookbla.americano.domain.payment.controller;

import com.bookbla.americano.domain.payment.controller.dto.request.AppleNotificationRequest;
import com.bookbla.americano.domain.payment.service.PaymentService;
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

    private final PaymentService paymentService;

    @PostMapping("/in-app/apple/notification")
    public ResponseEntity<Void> receiveAppleNotification(
            @RequestBody AppleNotificationRequest request
    ) {
        // 애플로부터 들어온 환불 정보는 모두 저장해두고 싶었으나
        // 예외가 터지면 트랜잭션 커밋이 안 돼, 일단 이렇게 구현해놨습니다
        boolean isSuccess = paymentService.receiveAppleNotification(request.getSignedPayload());
        if (isSuccess) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
