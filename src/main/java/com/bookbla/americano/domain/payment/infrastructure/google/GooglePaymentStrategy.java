package com.bookbla.americano.domain.payment.infrastructure.google;

import com.bookbla.americano.domain.payment.controller.dto.request.GooglePaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.enums.PaymentTable;
import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.infrastructure.google.api.dto.response.GooglePaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.repository.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GooglePaymentStrategy {

    private final GoogleCertificationProvider googleCertificationProvider;

    public Payment getPaymentInformation(GooglePaymentInAppPurchaseRequest request) {
        GooglePaymentPurchaseResponse response = googleCertificationProvider.getPurchaseReceipt(
            request.getProductId(), request.getPurchaseToken());

        PaymentTable paymentTable = PaymentTable.from(response.getProductId());

        return Payment.builder()
            .money(paymentTable.getPrice())
            .bookmarks(paymentTable.getCount())
            .paymentType(PaymentType.GOOGLE)
            .receipt(response.getOrderId())
            .build();
    }

}
