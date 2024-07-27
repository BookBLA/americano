package com.bookbla.americano.domain.school.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.response.MemberInvitationResponse;
import com.bookbla.americano.domain.school.controller.docs.InvitationControllerDocs;
import com.bookbla.americano.domain.school.controller.dto.request.InvitationCodeEntryRequest;
import com.bookbla.americano.domain.school.controller.dto.response.InvitationResponse;
import com.bookbla.americano.domain.school.service.InvitationService;
import io.swagger.v3.oas.annotations.Parameter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InvitationController implements InvitationControllerDocs {

    private final InvitationService invitationService;

    @GetMapping("/invitation-code")
    public ResponseEntity<MemberInvitationResponse> readInvitationCode(@User LoginUser loginUser) {
        MemberInvitationResponse response = invitationService.getInvitationCode(loginUser.getMemberId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/invitation-code")
    public ResponseEntity<InvitationResponse> entryInvitationCode(
            @User LoginUser loginUser,
            @RequestBody @Valid InvitationCodeEntryRequest request
    ) {
        InvitationResponse invitationResponse = invitationService.entryInvitationCode(loginUser.getMemberId(), request);
        return ResponseEntity.ok(invitationResponse);
    }

}
