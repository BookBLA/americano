package com.bookbla.americano.domain.member.controller;


import com.bookbla.americano.domain.member.controller.docs.ProfileImageTypeControllerDocs;
import com.bookbla.americano.domain.member.controller.dto.response.ProfileImageTypeReadResponse;
import com.bookbla.americano.domain.member.service.ProfileImageTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profiles")
public class ProfileImageTypeController implements ProfileImageTypeControllerDocs {

    private final ProfileImageTypeService profileImageTypeService;

    @GetMapping
    public ResponseEntity<ProfileImageTypeReadResponse> readProfileImageTypes() {
        return ResponseEntity.ok(profileImageTypeService.readStyles());
    }

}
