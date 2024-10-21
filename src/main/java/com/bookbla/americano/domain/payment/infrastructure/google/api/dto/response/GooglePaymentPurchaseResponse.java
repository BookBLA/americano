package com.bookbla.americano.domain.payment.infrastructure.google.api.dto.response;

import com.google.api.services.androidpublisher.model.ProductPurchase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Builder
public class GooglePaymentPurchaseResponse {

    private String kind;
    private String purchaseTimeMillis;
    private int purchaseState;
    private int consumptionState;
    private String orderId;
    private int acknowledgementState;
    private String purchaseToken;
    private String productId;

    public static GooglePaymentPurchaseResponse from(ProductPurchase purchase) {
        return builder()
            .kind(purchase.getKind())
            .purchaseTimeMillis(purchase.getPurchaseTimeMillis().toString())
            .purchaseState(purchase.getPurchaseState())
            .consumptionState(purchase.getConsumptionState())
            .orderId(purchase.getOrderId())
            .acknowledgementState(purchase.getAcknowledgementState())
            .purchaseToken(purchase.getPurchaseToken())
            .productId(purchase.getProductId())
            .build();
    }
}
