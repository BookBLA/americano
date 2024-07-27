package com.bookbla.americano.domain.test.controller.docs;

import com.bookbla.americano.domain.test.controller.dto.request.TestSignUpRequest;
import com.bookbla.americano.domain.test.controller.dto.response.TestSignUpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "개발용", description = "개발시 사용하는 API 목록")
public interface TestControllerDocs {

    @Operation(summary = "테스트 회원 토큰 발급")
    @ApiResponse(
            responseCode = "200"
    )
    @PostMapping("/sign-up/admin")
    ResponseEntity<TestSignUpResponse> testSignUpAdmin(
            @RequestBody TestSignUpRequest testSignUpRequest
    );

    @ApiResponse(
            responseCode = "200"
    )
    @Operation(summary = "카카오 회원 토큰 발급")
    @PostMapping("/sign-up/kakao")
    ResponseEntity<TestSignUpResponse> testSignUpKakao(
            @RequestBody TestSignUpRequest testSignUpRequest
    );

}
