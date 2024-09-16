package com.bookbla.americano.domain.payment.infrastructure.apple;

import com.apple.itunes.storekit.model.NotificationTypeV2;
import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.bookbla.americano.domain.payment.enums.PaymentTable;
import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.infrastructure.apple.tokens.DecodedTokenPayload;
import com.bookbla.americano.domain.payment.repository.Payment;
import com.bookbla.americano.domain.payment.service.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ApplePaymentStrategy implements PaymentStrategy {

    private final AppleTokenProvider appleTokenProvider;
    private final AppleLibraryProvider appleLibraryProvider;

    @Override
    public Payment getPaymentInformation(String transactionId) {
        String signedTransactionInfo = appleLibraryProvider.getSignedTransactionInfo(transactionId);

        appleLibraryProvider.validateTransaction(signedTransactionInfo);

        DecodedTokenPayload payload = appleTokenProvider.decodePayload(signedTransactionInfo);
        PaymentTable paymentTable = PaymentTable.from(payload.getProductId());

        return Payment.builder()
                .money(paymentTable.getPrice())
                .bookmark(paymentTable.getCount())
                .paymentType(PaymentType.APPLE)
                .receipt(transactionId)
                .build();
    }

    @Override
    public void getNotificationInformation(String id) {
        ResponseBodyV2DecodedPayload notificationPayload = appleLibraryProvider.getNotificationPayload(id);
    }

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.APPLE;
    }
}
