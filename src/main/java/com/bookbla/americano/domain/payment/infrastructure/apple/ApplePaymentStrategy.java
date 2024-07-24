package com.bookbla.americano.domain.payment.infrastructure.apple;

import com.bookbla.americano.domain.payment.enums.PaymentTable;
import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.infrastructure.apple.api.ApplePaymentApiClient;
import com.bookbla.americano.domain.payment.infrastructure.apple.api.dto.ApplePaymentTransactionInfoResponse;
import com.bookbla.americano.domain.payment.repository.entity.Payment;
import com.bookbla.americano.domain.payment.service.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.bookbla.americano.domain.payment.enums.PaymentType.APPLE;

/*
 * reference : https://developer.apple.com/videos/play/wwdc2021/10174
 * */
@RequiredArgsConstructor
@Component
public class ApplePaymentStrategy implements PaymentStrategy {

    private final ApplePaymentApiClient apiClient;
    private final AppleTokenProvider appleTokenProvider;
    private final AppleCertificationProvider appleCertificationProvider;

    @Override
    public Payment getPaymentInformation(String transactionId) {
        String tokenValue = "Bearer " + appleTokenProvider.createToken();
        ApplePaymentTransactionInfoResponse response = apiClient.getTransactionInformation(tokenValue, transactionId);
        String responseToken = response.getSignedTransactionInfo();

        ApplePaymentTransactionInfoHeader header = appleTokenProvider.decodeHeader(responseToken);
        appleCertificationProvider.validateCertificate(header.getX5c());

        ApplePaymentTransactionInfoPayload payload = appleTokenProvider.decodePayload(responseToken);
        PaymentTable paymentTable = PaymentTable.from(payload.getInAppPurchaseProductId());

        return Payment.builder()
                .money(paymentTable.getPrice())
                .bookmark(paymentTable.getCount())
                .paymentType(APPLE)
                .build();
    }

    @Override
    public PaymentType getPaymentType() {
        return APPLE;
    }
}
