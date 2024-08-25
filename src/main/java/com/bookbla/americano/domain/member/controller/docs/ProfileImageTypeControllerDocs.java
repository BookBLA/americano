package com.bookbla.americano.domain.member.controller.docs;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.response.ProfileImageTypeReadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "기본 프로필 이미지 관련 정보")
public interface ProfileImageTypeControllerDocs {

    @Operation(summary = "사용자와 같은 성별의 기본 프로필 설정 값들을 읽어들입니다.")
    @ApiResponse(responseCode = "200")
    @GetMapping
    ResponseEntity<ProfileImageTypeReadResponse> readMemberProfileGenderTypes(
            @Parameter(hidden = true) @User LoginUser loginUser
    );
}
