package com.bookbla.americano.domain.payment.infrastructure.google;

import com.bookbla.americano.domain.payment.controller.dto.request.GooglePaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.enums.PaymentTable;
import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.infrastructure.google.api.dto.response.GooglePaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.repository.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class GooglePaymentStrategy {

    private final GoogleCertificationProvider googleCertificationProvider;

    public Payment getPaymentInformation(GooglePaymentInAppPurchaseRequest request) {
        GooglePaymentPurchaseResponse response = googleCertificationProvider.getPurchaseReceipt(
            request.getProductId(), request.getPurchaseToken());

        log.info("getPaymentInformation -> response.getProductId : " + response.getProductId());
        PaymentTable paymentTable = PaymentTable.from(response.getProductId());

        return Payment.builder()
            .money(paymentTable.getPrice())
            .bookmark(paymentTable.getCount())
            .paymentType(PaymentType.GOOGLE)
            .receipt(response.getOrderId())
            .build();
    }

}
