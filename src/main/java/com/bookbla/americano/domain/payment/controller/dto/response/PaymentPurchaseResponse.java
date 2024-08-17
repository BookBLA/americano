package com.bookbla.americano.domain.payment.controller.dto.response;

import com.bookbla.americano.domain.payment.repository.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PaymentPurchaseResponse {

    private final int bookmarks;
    private final int price;

    public static PaymentPurchaseResponse from(Payment payment) {
        return new PaymentPurchaseResponse(payment.getBookmarks(), payment.getMoney().intValue());
    }
}
