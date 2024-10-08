package com.bookbla.americano.domain.member.controller;

import java.net.URI;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.docs.MemberBookControllerDocs;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookQuizUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookReviewUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponses;
import com.bookbla.americano.domain.member.service.MemberBookService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member-books")
public class MemberBookController implements MemberBookControllerDocs {

    private final MemberBookService memberBookService;

    @PostMapping
    public ResponseEntity<MemberBookCreateResponse> addMemberBook(
            @User LoginUser loginUser,
            @RequestBody @Valid MemberBookCreateRequest memberBookCreateRequest
    ) {
        MemberBookCreateResponse memberBookCreateResponse = memberBookService.addMemberBook(
                loginUser.getMemberId(), memberBookCreateRequest);
        return ResponseEntity
                .created(URI.create("/member-books/" + memberBookCreateResponse.getMemberBookId()))
                .body(memberBookCreateResponse);
    }

    @GetMapping
    public ResponseEntity<MemberBookReadResponses> readMemberBooks(@User LoginUser loginUser) {
        MemberBookReadResponses memberBookReadResponses = memberBookService.readMemberBooks(
                loginUser.getMemberId());
        return ResponseEntity.ok(memberBookReadResponses);
    }

    @GetMapping("/{memberBookId}")
    public ResponseEntity<MemberBookReadResponse> readMemberBook(
            @User LoginUser loginUser,
            @PathVariable Long memberBookId
    ) {
        MemberBookReadResponse memberBookReadResponse = memberBookService.readMemberBook(loginUser.getMemberId(), memberBookId);
        return ResponseEntity.ok(memberBookReadResponse);
    }

    @PutMapping("/{memberBookId}")
    public ResponseEntity<Void> updateMemberBook(
            @PathVariable Long memberBookId, @User LoginUser loginUser,
            @RequestBody @Valid MemberBookUpdateRequest memberBookUpdateRequest
    ) {
        memberBookService.updateMemberBook(memberBookUpdateRequest, memberBookId,
                loginUser.getMemberId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{memberBookId}/review")
    public ResponseEntity<Void> updateReview(
            @PathVariable Long memberBookId, @User LoginUser loginUser,
            @RequestBody @Valid MemberBookReviewUpdateRequest memberBookReviewUpdateRequest
    ) {
        memberBookService.updateMemberBookReview(memberBookReviewUpdateRequest, memberBookId,
                loginUser.getMemberId());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{memberBookId}/quiz")
    public ResponseEntity<Void> updateQuiz(
            @PathVariable Long memberBookId,
            @User LoginUser loginUser,
            @RequestBody @Valid MemberBookQuizUpdateRequest memberBookQuizUpdateRequest
    ) {
        memberBookService.updateMemberBookQuiz(memberBookQuizUpdateRequest, memberBookId,
                loginUser.getMemberId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{memberBookId}")
    public ResponseEntity<Void> deleteMemberBook(
            @User LoginUser loginUser,
            @PathVariable Long memberBookId
    ) {
        memberBookService.deleteMemberBook(loginUser.getMemberId(), memberBookId);
        return ResponseEntity.noContent().build();
    }
}
