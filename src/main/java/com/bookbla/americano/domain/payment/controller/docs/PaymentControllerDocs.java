package com.bookbla.americano.domain.payment.controller.docs;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.payment.controller.dto.PaymentBookmarkRequest;
import com.bookbla.americano.domain.payment.controller.dto.PaymentPurchaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "결제", description = "결제와 관련된 API 모음")
public interface PaymentControllerDocs {

    @Operation(summary = "인앱결제 모음입니당")
    @ApiResponse(
            responseCode = "200",
            description = "payType : apple,google"
    )
    @PostMapping
    ResponseEntity<PaymentPurchaseResponse> orderBookmark(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @Valid @RequestBody PaymentBookmarkRequest request,
            @PathVariable String payType
    );

}
