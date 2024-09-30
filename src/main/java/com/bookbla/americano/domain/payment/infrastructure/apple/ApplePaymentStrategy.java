package com.bookbla.americano.domain.payment.infrastructure.apple;

import com.apple.itunes.storekit.model.ResponseBodyV2DecodedPayload;
import com.bookbla.americano.domain.payment.enums.PaymentTable;
import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.infrastructure.apple.tokens.DecodedTokenPayload;
import com.bookbla.americano.domain.payment.repository.entity.Payment;
import com.bookbla.americano.domain.payment.repository.entity.PaymentNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ApplePaymentStrategy {

    private final AppleTokenProvider appleTokenProvider;
    private final AppleLibraryProvider appleLibraryProvider;

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

    public PaymentNotification getNotificationInformation(String id) {
        ResponseBodyV2DecodedPayload notificationPayload = appleLibraryProvider.getNotificationDecodedPayload(id);
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
}
