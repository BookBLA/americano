package com.bookbla.americano.domain.payment.infrastructure.apple.tokens;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class DecodedTokenPayload {

    private String transactionId;
    private String originalTransactionId;
    private String webOrderLineItemId;
    private String bundleId; // app.id
    private String productId; // app.id.productId
    private String subscriptionGroupIdentifier;
    private Long purchaseDate; // 유닉스 타임 스탬프
    private Long originalPurchaseDate; // 유닉스 타임 스탬프
    private Long expiresDate; // 유닉스 타임 스탬프
    private Long quantity;
    private String type;
    private String appAccountToken;
    private Long revocationReason;
    private Long offerType; // 1 = intro offer, 2 = promotional offer, 3 = offer code
    private String offerIdentifier; // present for offerType 2 and 3

    public String getInAppPurchaseProductId() {
        return productId.replace(bundleId + ".", "");
    }
}
