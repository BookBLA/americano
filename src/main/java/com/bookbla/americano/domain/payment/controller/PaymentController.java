package com.bookbla.americano.domain.payment.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.payment.controller.docs.PaymentControllerDocs;
import com.bookbla.americano.domain.payment.controller.dto.request.PaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.controller.dto.response.PaymentPurchaseResponse;
import com.bookbla.americano.domain.payment.service.PaymentService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/payments")
@RestController
public class PaymentController implements PaymentControllerDocs {

    private final PaymentService paymentService;

    @PostMapping("/in-app/{payType}")
    public ResponseEntity<PaymentPurchaseResponse> orderBookmark(
            @User LoginUser loginUser,
            @Valid @RequestBody PaymentInAppPurchaseRequest request,
            @PathVariable String payType
    ) {
        PaymentPurchaseResponse paymentPurchaseResponse = paymentService.orderBookmark(payType, request, loginUser.getMemberId());
        return ResponseEntity.ok(paymentPurchaseResponse);
    }

}
