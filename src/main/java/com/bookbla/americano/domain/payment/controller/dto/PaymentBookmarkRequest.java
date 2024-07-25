package com.bookbla.americano.domain.payment.controller.dto;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class PaymentBookmarkRequest {

    @NotBlank(message = "영수증 정보가 입력되지 않았습니다.")
    private String transactionId;

}
