package com.bookbla.americano.domain.payment.controller.docs;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.payment.controller.dto.request.PaymentInAppPurchaseRequest;
import com.bookbla.americano.domain.payment.controller.dto.response.PaymentPurchaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "결제", description = "결제와 관련된 API 모음")
public interface PaymentControllerDocs {

    @Operation(summary = "인앱결제 API입니다")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "결제 성공"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 결제 유형입니다."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 결제 메뉴입니다(wrong transactionId)"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "애플 인증서를 역직렬화 하는데 실패하였습니다."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "루트 인증서가 애플 인증서와 일치하지 않습니다."
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "인증서 체인이 서로 암호화되지 않았습니다."
            ),
    })
    @PostMapping
    ResponseEntity<PaymentPurchaseResponse> orderBookmarkForApple(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @Valid @RequestBody PaymentInAppPurchaseRequest request
//            @PathVariable @Parameter(name = "payType", description = "인앱결제 유형입니다", example = "apple, google") String payType
    );
}
