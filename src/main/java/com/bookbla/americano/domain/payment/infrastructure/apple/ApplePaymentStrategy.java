package com.bookbla.americano.domain.payment.infrastructure.apple;

import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.bookbla.americano.domain.payment.enums.PaymentTable;
import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.infrastructure.apple.tokens.DecodedTokenPayload;
import com.bookbla.americano.domain.payment.repository.Payment;
import com.bookbla.americano.domain.payment.repository.PaymentNotification;
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
                .information(signedTransactionInfo)
                .build();
    }

    @Override
    public PaymentNotification getNotificationInformation(String id) {
        ResponseBodyV2DecodedPayload notificationPayload = appleLibraryProvider.getNotificationPayload(id);
        String notificationType = notificationPayload.getNotificationType().getValue();
        String signedTransactionInfo = notificationPayload.getData().getSignedTransactionInfo();

        appleLibraryProvider.validateTransaction(signedTransactionInfo);

        DecodedTokenPayload payload = appleTokenProvider.decodePayload(signedTransactionInfo);

        return PaymentNotification.builder()
                .information(signedTransactionInfo)
                .type(notificationType)
                .receipt(payload.getTransactionId())
                .productId(payload.getProductId())
                .build();
    }

    @Override
    public PaymentType getPaymentType() {
        return PaymentType.APPLE;
    }
}
