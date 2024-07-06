package com.bookbla.americano.domain.school.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.school.controller.dto.response.SchoolReadResponse;
import com.bookbla.americano.domain.school.service.SchoolService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/schools")
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ResponseEntity<SchoolReadResponse> readSchool() {
        SchoolReadResponse schoolReadResponse = schoolService.readSchool();
        return ResponseEntity.ok(schoolReadResponse);
    }

    @GetMapping("/members")
    public ResponseEntity<SchoolInvitationResponse> readSchoolInformation(
            @Parameter(hidden = true) @User LoginUser loginUser
    ) {
        SchoolInvitationResponse response = schoolService.getSchoolInformation(loginUser.getMemberId());
        return ResponseEntity.ok(response);
    }
}
