package com.bookbla.americano.domain.payment.infrastructure.apple.api.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class ApplePaymentTransactionInfoResponse {

    private String signedTransactionInfo;

}
