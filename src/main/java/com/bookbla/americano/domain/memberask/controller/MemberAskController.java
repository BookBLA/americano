package com.bookbla.americano.domain.memberask.controller;


import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.domain.memberask.controller.dto.request.MemberAskCreateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.request.MemberAskUpdateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.response.MemberAskResponse;
import com.bookbla.americano.domain.memberask.service.MemberAskService;
import io.swagger.v3.oas.annotations.Parameter;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/member-ask")
public class MemberAskController {

    private final MemberAskService memberAskService;

    @PostMapping
    public ResponseEntity<Void> createMemberAsk(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberAskCreateRequest memberAskCreateRequest
    ) {
        MemberAskResponse memberAskResponse = memberAskService.createMemberAsk(
                loginUser.getMemberId(), memberAskCreateRequest);
        return ResponseEntity.created(
                        URI.create(memberAskResponse.getMemberAskResponseId().toString()))
                .build();
    }

    @GetMapping
    public ResponseEntity<MemberAskResponse> readMemberAsk(
            @Parameter(hidden = true) @User LoginUser loginUser) {
        MemberAskResponse memberAskResponse = memberAskService.readMemberAsk(
                loginUser.getMemberId());
        return ResponseEntity.ok(memberAskResponse);
    }

    @PutMapping
    public ResponseEntity<Void> updateMemberAsk(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberAskUpdateRequest memberAskUpdateRequest) {
        memberAskService.updateMemberAsk(loginUser.getMemberId(), memberAskUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}
