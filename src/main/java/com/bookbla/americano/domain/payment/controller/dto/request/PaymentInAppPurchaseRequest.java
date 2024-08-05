package com.bookbla.americano.domain.payment.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class PaymentInAppPurchaseRequest {

    @Schema(name = "인앱결제 후 받은 영수증 정보", example = "109886")
    @NotBlank(message = "영수증 정보가 입력되지 않았습니다.")
    private String transactionId;

}
