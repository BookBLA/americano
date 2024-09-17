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
public class GooglePaymentInAppPurchaseRequest {

    @Schema(example = "bookmark_01")
    @NotBlank(message = "인앱 상품 ID가 입력되지 않았습니다.")
    private String productId;

    @Schema(example = "109886")
    @NotBlank(message = "인앱 상품 토큰이 입력되지 않았습니다.")
    private String purchaseToken;

}
