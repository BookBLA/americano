package com.bookbla.americano.domain.member.controller.docs;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookQuizUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookReviewUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "회원 도서", description = "도서 등록/수정/조회/삭제 관련 API입니다.")
public interface MemberBookControllerDocs {

    @Operation(summary = "회원 도서 등록")
    @ApiResponse(responseCode = "201")
    @PostMapping
    ResponseEntity<MemberBookCreateResponse> addMemberBook(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberBookCreateRequest memberBookCreateRequest
    );

    @Operation(summary = "회원의 모든 도서 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    ResponseEntity<MemberBookReadResponses> readMemberBooks(
            @Parameter(hidden = true) @User LoginUser loginUser
    );

    @Operation(summary = "회원 도서 상세 조회")
    @ApiResponse(responseCode = "200")
    @GetMapping
    ResponseEntity<MemberBookReadResponse> readMemberBook(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @PathVariable Long memberBookId
    );

    @Operation(summary = "회원 도서 모든 내용(퀴즈 및 답변, 감상문) 수정")
    @ApiResponse(responseCode = "204")
    @PutMapping
    ResponseEntity<Void> updateMemberBook(
            @PathVariable Long memberBookId,
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberBookUpdateRequest memberBookUpdateRequest
    );

    @Operation(summary = "회원 도서의 퀴즈 및 답변 수정")
    @ApiResponse(responseCode = "204")
    @PatchMapping
    ResponseEntity<Void> updateQuiz(
            @PathVariable Long memberBookId,
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberBookQuizUpdateRequest memberBookQuizUpdateRequest
    );

    @Operation(summary = "회원 도서의 감상문 수정")
    @ApiResponse(responseCode = "204")
    @PatchMapping
    ResponseEntity<Void> updateReview(
            @PathVariable Long memberBookId,
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid MemberBookReviewUpdateRequest memberBookReviewUpdateRequest
    );

    @Operation(summary = "회원 도서 삭제")
    @ApiResponse(responseCode = "204")
    @DeleteMapping
    ResponseEntity<Void> deleteMemberBook(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @PathVariable Long memberBookId
    );
}
