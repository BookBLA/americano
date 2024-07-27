package com.bookbla.americano.domain.member.controller.docs;

import com.bookbla.americano.domain.member.controller.dto.response.StylesResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

public interface StyleControllerDocs {

    @Operation(summary = "스타일 관련 모든 값들을 읽어들입니다.")
    @ApiResponse(responseCode = "200")
    @GetMapping
    ResponseEntity<StylesResponse> readStyles();

}
