package com.bookbla.americano.domain.payment.infrastructure.google;

import com.bookbla.americano.domain.payment.controller.dto.request.GooglePaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.enums.PaymentTable;
import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.infrastructure.google.api.dto.response.GooglePaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.repository.Payment;
import com.bookbla.americano.domain.payment.repository.PaymentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class GooglePaymentStrategy {

    private final GoogleCertificationProvider googleCertificationProvider;
    private final PaymentRepository paymentRepository;

    public Payment getPaymentInformation(GooglePaymentInAppPurchaseRequest request) {
        GooglePaymentPurchaseResponse response = googleCertificationProvider.getPurchaseReceipt(
            request.getProductId(), request.getPurchaseToken());

        PaymentTable paymentTable = PaymentTable.from(request.getProductId());

        Optional<Payment> payment = paymentRepository.findByOrderId(response.getOrderId());

        return payment.orElseGet(() -> Payment.builder()
            .money(paymentTable.getPrice())
            .bookmark(paymentTable.getCount())
            .paymentType(PaymentType.GOOGLE)
            .orderId(response.getOrderId())
            .purchaseToken(request.getPurchaseToken())
            .build());

    }

}
