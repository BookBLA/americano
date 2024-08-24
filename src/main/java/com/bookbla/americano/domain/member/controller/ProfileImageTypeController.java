package com.bookbla.americano.domain.member.controller;


import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
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
@RequestMapping("/profile-image-types")
public class ProfileImageTypeController implements ProfileImageTypeControllerDocs {

    private final ProfileImageTypeService profileImageTypeService;

    @GetMapping
    public ResponseEntity<ProfileImageTypeReadResponse> readProfileImageTypes() {
        return ResponseEntity.ok(profileImageTypeService.readAll());
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileImageTypeReadResponse> readMemberProfileGenderTypes(
            @User LoginUser loginUser
    ) {
        var profileImageTypeReadResponse = profileImageTypeService.readMemberGenderProfileImageTypes(
                loginUser.getMemberId());
        return ResponseEntity.ok(profileImageTypeReadResponse);
    }
}
