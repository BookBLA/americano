package com.bookbla.americano.domain.payment.infrastructure.apple.api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ApplePaymentTransactionInfoResponse {

    private String signedTransactionId;

}
