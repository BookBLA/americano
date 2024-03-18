package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponses;
import com.bookbla.americano.domain.member.service.MemberBookService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member-books")
public class MemberBookController {

    private final MemberBookService memberBookService;

    @PostMapping
    public ResponseEntity<MemberBookCreateResponse> addMemberBook(
            @LoginUser Long memberId,
            @RequestBody @Valid MemberBookCreateRequest memberBookCreateRequest
    ) {
        MemberBookCreateResponse memberBookCreateResponse = memberBookService.addMemberBook(memberId, memberBookCreateRequest);
        return ResponseEntity
                .created(URI.create("/member-books/" + memberBookCreateResponse.getMemberBookId()))
                .body(memberBookCreateResponse);
    }

    @GetMapping
    public ResponseEntity<MemberBookReadResponses> readMemberBooks(@LoginUser Long memberId) {
        MemberBookReadResponses memberBookReadResponses = memberBookService.readMemberBooks(memberId);
        return ResponseEntity.ok(memberBookReadResponses);
    }

    @DeleteMapping("/{memberBookId}")
    public ResponseEntity<Void> deleteMemberBook(@LoginUser Long memberId, @PathVariable Long memberBookId) {
        memberBookService.deleteMemberBook(memberId, memberBookId);
        return ResponseEntity.noContent().build();
    }

}
