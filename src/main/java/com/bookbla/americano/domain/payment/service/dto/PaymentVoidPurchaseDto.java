package com.bookbla.americano.domain.payment.service.dto;

import com.bookbla.americano.domain.payment.enums.PaymentType;
import com.bookbla.americano.domain.payment.enums.VoidedReason;
import com.bookbla.americano.domain.payment.enums.VoidedSource;
import com.bookbla.americano.domain.payment.repository.entity.PaymentVoidPurchase;
import com.google.api.services.androidpublisher.model.VoidedPurchase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PaymentVoidPurchaseDto {

    private String orderId;
    private String purchaseToken;
    private VoidedReason voidedReason;
    private VoidedSource voidedSource;
    private PaymentType paymentType;

    public static PaymentVoidPurchase toPaymentVoidPurchase(VoidedPurchase voidedPurchase) {
        return PaymentVoidPurchase.builder()
            .orderId(voidedPurchase.getOrderId())
            .purchaseToken(voidedPurchase.getPurchaseToken())
            .voidedReason(VoidedReason.from(voidedPurchase.getVoidedReason()))
            .voidedSource(VoidedSource.from(voidedPurchase.getVoidedSource()))
            .paymentType(PaymentType.GOOGLE)
            .build();
    }


}
