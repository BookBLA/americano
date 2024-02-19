package com.bookbla.americano.domain.memberask.controller.dto;


import com.bookbla.americano.domain.memberask.controller.dto.request.MemberAskCreateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.response.MemberAskResponse;
import com.bookbla.americano.domain.memberask.service.MemberAskService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members/member-ask")
public class MemberAskController {

    private final MemberAskService memberAskService;

    @PostMapping
    public ResponseEntity<Void> createMemberAsk(Long memberId, MemberAskCreateRequest memberAskCreateRequest) {
        MemberAskResponse memberAskResponse = memberAskService.createMemberAsk(memberId, memberAskCreateRequest);
        return ResponseEntity.created(URI.create(memberAskResponse.getMemberAskResponseId().toString()))
                .build();
    }

}
