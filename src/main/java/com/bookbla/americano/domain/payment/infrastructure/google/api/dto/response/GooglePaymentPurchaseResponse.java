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
    private String developerPayload;
    private String orderId;
    private int purchaseType;
    private int acknowledgementState;
    private String purchaseToken;
    private String productId;
    private int quantity;
    private String obfuscatedExternalAccountId;
    private String obfuscatedExternalProfileId;
    private String regionCode;
//    private int refundableQuantity;

    public static GooglePaymentPurchaseResponse from(ProductPurchase purchase) {
        return builder()
            .kind(purchase.getKind())
            .purchaseTimeMillis(purchase.getPurchaseTimeMillis().toString())
            .purchaseState(purchase.getPurchaseState())
            .consumptionState(purchase.getConsumptionState())
            .developerPayload(purchase.getDeveloperPayload())
            .orderId(purchase.getOrderId())
            .purchaseType(purchase.getPurchaseType())
            .acknowledgementState(purchase.getAcknowledgementState())
            .purchaseToken(purchase.getPurchaseToken())
            .productId(purchase.getProductId())
            .quantity(purchase.getQuantity())
            .obfuscatedExternalAccountId(purchase.getObfuscatedExternalAccountId())
            .obfuscatedExternalProfileId(purchase.getObfuscatedExternalProfileId())
            .regionCode(purchase.getRegionCode())
            .build();
    }
}
