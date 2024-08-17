package com.bookbla.americano.domain.payment.infrastructure.apple.api;

import com.bookbla.americano.domain.payment.infrastructure.apple.api.dto.ApplePaymentTransactionInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import static org.apache.http.HttpHeaders.AUTHORIZATION;

/*
 * https://developer.apple.com/documentation/appstoreserverapi/get_transaction_info
 * */
@FeignClient(name = "apple-payment", url = "${payments.apple.server-domain}/inApps/v1")
public interface ApplePaymentApiClient {

    @GetMapping("/transactions/{transactionId}")
    ApplePaymentTransactionInfoResponse getTransactionInformation(
            @RequestHeader(AUTHORIZATION) String token,
            @PathVariable("transactionId") String transactionId
    );
}
