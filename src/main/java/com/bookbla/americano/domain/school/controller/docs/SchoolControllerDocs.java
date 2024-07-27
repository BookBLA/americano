package com.bookbla.americano.domain.school.controller.docs;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.school.controller.dto.response.SchoolInvitationResponse;
import com.bookbla.americano.domain.school.controller.dto.response.SchoolReadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "학교 관련", description = "학교 관련 API 모음입니다.")
public interface SchoolControllerDocs {

    @Operation(summary = "모든 학교 목록을 반환")
    @GetMapping
    ResponseEntity<SchoolReadResponse> readSchool();

    @Operation(summary = "해당 학교의 가입 회원 목록을 반환")
    @GetMapping
    ResponseEntity<SchoolInvitationResponse> readSchoolInformation(
            @Parameter(hidden = true) @User LoginUser loginUser
    );
}
