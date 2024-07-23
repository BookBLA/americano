package com.bookbla.americano.domain.payment.infrastructure.apple;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class ApplePaymentTransactionInfoHeader {

    private String alg;
    private List<String> x5c;

}
