package com.bookbla.americano.domain.school.controller;

import com.bookbla.americano.domain.school.controller.dto.response.SchoolReadResponse;
import com.bookbla.americano.domain.school.service.SchoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ResponseEntity<SchoolReadResponse> readSchool() {
        SchoolReadResponse schoolReadResponse = schoolService.readSchool();
        return ResponseEntity.ok(schoolReadResponse);
    }

}
