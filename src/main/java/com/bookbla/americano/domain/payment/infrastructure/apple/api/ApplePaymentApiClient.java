package com.bookbla.americano.domain.payment.infrastructure.apple.api;

import com.bookbla.americano.domain.payment.infrastructure.apple.api.dto.ApplePaymentTransactionInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "apple-payment", url = "${payments.apple.server-domain}")
public interface ApplePaymentApiClient {

    @GetMapping("/transactions/{transactionId}")
    ApplePaymentTransactionInfoResponse getTransactionInformation(
            @RequestHeader("Authorization") String token,
            @PathVariable("transactionId") String transactionId
    );

}
