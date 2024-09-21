package com.bookbla.americano.domain.payment.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.payment.controller.docs.PaymentControllerDocs;
import com.bookbla.americano.domain.payment.controller.dto.request.GooglePaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.controller.dto.request.ApplePaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.controller.dto.response.PaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/payments")
@RestController
public class PaymentController implements PaymentControllerDocs {

    private final PaymentService paymentService;

    @PostMapping("/in-app/apple")
    public ResponseEntity<PaymentPurchaseResponse> orderBookmarkForApple(
        @User LoginUser loginUser,
        @Valid @RequestBody ApplePaymentInAppPurchaseRequest request
    ) {
        PaymentPurchaseResponse paymentPurchaseResponse = paymentService.orderBookmarkForApple(
            request, loginUser.getMemberId());
        return ResponseEntity.ok(paymentPurchaseResponse);
    }

    @PostMapping("/in-app/google")
    public ResponseEntity<PaymentPurchaseResponse> orderBookmarkForGoogle(
        @Parameter(hidden = true) @User LoginUser loginUser,
        @Valid @RequestBody GooglePaymentInAppPurchaseRequest request
    ) {
        PaymentPurchaseResponse paymentPurchaseResponse = paymentService.orderBookmarkForGoogle(
            request, loginUser.getMemberId());
        return ResponseEntity.ok(paymentPurchaseResponse);
    }
}
