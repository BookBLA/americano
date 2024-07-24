package com.bookbla.americano.domain.payment.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.payment.controller.dto.PaymentBookmarkRequest;
import com.bookbla.americano.domain.payment.controller.dto.PaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/payments")
@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/${payType}")
    public ResponseEntity<PaymentPurchaseResponse> orderBookmark(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @Valid @RequestBody PaymentBookmarkRequest request,
            @PathVariable String payType
    ) {
        PaymentPurchaseResponse paymentPurchaseResponse = paymentService.orderBookmark(payType, request, loginUser.getMemberId());
        return ResponseEntity.ok(paymentPurchaseResponse);
    }
}
