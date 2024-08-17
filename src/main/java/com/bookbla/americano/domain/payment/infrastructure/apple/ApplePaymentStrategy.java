package com.bookbla.americano.domain.payment.infrastructure.apple;

import com.bookbla.americano.domain.payment.enums.PaymentTable;
import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.infrastructure.apple.api.ApplePaymentApiClient;
import com.bookbla.americano.domain.payment.infrastructure.apple.api.dto.ApplePaymentTransactionInfoResponse;
import com.bookbla.americano.domain.payment.infrastructure.apple.tokens.DecodedTokenHeader;
import com.bookbla.americano.domain.payment.infrastructure.apple.tokens.DecodedTokenPayload;
import com.bookbla.americano.domain.payment.repository.Payment;
import com.bookbla.americano.domain.payment.service.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ApplePaymentStrategy implements PaymentStrategy {

    private final ApplePaymentApiClient apiClient;
    private final AppleTokenProvider appleTokenProvider;
    private final AppleCertificationProvider appleCertificationProvider;

    @Override
    public Payment getPaymentInformation(String transactionId) {
        String tokenValue = "Bearer " + appleTokenProvider.createRequestToken();
        ApplePaymentTransactionInfoResponse response = apiClient.getTransactionInformation(tokenValue, transactionId);
        String responseToken = response.getSignedTransactionInfo();

        DecodedTokenHeader header = appleTokenProvider.decodeHeader(responseToken);
        appleCertificationProvider.validateCertificate(header.getX5c());

        DecodedTokenPayload payload = appleTokenProvider.decodePayload(responseToken);
        PaymentTable paymentTable = PaymentTable.from(payload.getInAppPurchaseProductId());

        return Payment.builder()
                .money(paymentTable.getPrice())
                .bookmarks(paymentTable.getCount())
                .paymentType(PaymentType.APPLE)
                .receipt(transactionId)
                .build();
    }

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.APPLE;
    }
}
