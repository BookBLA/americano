package com.bookbla.americano.domain.payment.infrastructure.apple.tokens;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class DecodedTokenPayload {

    private String transactionId;
    private String originalTransactionId;
    private String bundleId;
    private String productId;
    private long purchaseDate;
    private long originalPurchaseDate;
    private int quantity;
    private String type;
    private String inAppOwnershipType;
    private long signedDate;
    private String environment;
    private String transactionReason;
    private String storefront;
    private String storefrontId;
    private long price;
    private String currency;

}
