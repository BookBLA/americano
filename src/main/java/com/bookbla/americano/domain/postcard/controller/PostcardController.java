package com.bookbla.americano.domain.postcard.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.member.service.MemberPostcardService;
import com.bookbla.americano.domain.postcard.controller.dto.request.PostcardSendValidationRequest;
import com.bookbla.americano.domain.postcard.controller.dto.request.PostcardStatusUpdateRequest;
import com.bookbla.americano.domain.postcard.controller.dto.response.MemberPostcardFromResponse;
import com.bookbla.americano.domain.postcard.controller.dto.response.MemberPostcardResponse;
import com.bookbla.americano.domain.postcard.controller.dto.response.MemberPostcardToResponse;
import com.bookbla.americano.domain.postcard.controller.dto.response.PostcardSendValidateResponse;
import com.bookbla.americano.domain.postcard.controller.dto.response.PostcardStatusResponse;
import com.bookbla.americano.domain.postcard.enums.PostcardStatus;
import com.bookbla.americano.domain.postcard.service.PostcardService;
import com.bookbla.americano.domain.postcard.service.dto.request.SendPostcardRequest;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardTypeResponse;
import com.bookbla.americano.domain.postcard.service.dto.response.SendPostcardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/postcard")
@RequiredArgsConstructor
public class PostcardController {

    private final PostcardService postcardService;
    private final MemberPostcardService memberPostcardService;

    @Operation(summary = "사용자 Postcard 타입 별 개수 조회", description = "Free Postcard / Pay Postcard 타입 별 개수 조회")
    @GetMapping("")
    public ResponseEntity<MemberPostcardResponse> getMemberPostcardCount(
            @Parameter(hidden = true) @User LoginUser loginUser) {
        return ResponseEntity.ok(
                memberPostcardService.getMemberPostcardEachCount(loginUser.getMemberId()));
    }

    @Operation(summary = "보낸 엽서 조회", description = "사용자가 보낸 엽서 조회")
    @GetMapping("/from")
    public ResponseEntity<List<MemberPostcardFromResponse>> readPostcardsFromMember(
            @Parameter(hidden = true) @User LoginUser loginUser) {
        return ResponseEntity.ok(postcardService.getPostcardsFromMember(loginUser.getMemberId()));
    }

    @Operation(summary = "받은 엽서 조회", description = "사용자가 받은 엽서 조회")
    @GetMapping("/to")
    public ResponseEntity<List<MemberPostcardToResponse>> readPostcardsToMember(
            @Parameter(hidden = true) @User LoginUser loginUser) {
        return ResponseEntity.ok(postcardService.getPostcardsToMember(loginUser.getMemberId()));
    }

    @Operation(summary = "엽서 읽기", description = "받은 엽서 조회를 위한 엽서 사용 및 엽서 상태 (READ)로 변경")
    @PostMapping("/read/{postcardId}")
    public void usePostcard(@Parameter(hidden = true) @User LoginUser loginUser,
                            @PathVariable Long postcardId) {
        postcardService.readMemberPostcard(loginUser.getMemberId(), postcardId);
    }

    @Operation(summary = "Postcard 상태 업데이트", description = "Body의 postcardId를 가진 엽서의 상태 업데이트. Body의 status 값으로 해당 엽서의 상태(PostcardStatus)를 변경함.")
    @PostMapping("/status")
    public void updatePostcardStatus(@Parameter(hidden = true) @User LoginUser loginUser,
                                     @RequestBody @Valid PostcardStatusUpdateRequest request) {
        postcardService.updatePostcardStatus(loginUser.getMemberId(), request.getPostcardId(),
                PostcardStatus.from(request.getStatus()));
    }

    @Operation(summary = "Postcard 상태 조회", description = "postcardId를 이용하여 엽서 상태를 반환")
    @PostMapping("/status/{postcardId}")
    public PostcardStatusResponse updatePostcardStatus(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @PathVariable Long postcardId) {
        return PostcardStatusResponse.from(postcardService.getPostcardStatus(postcardId));
    }

    @PostMapping("/send")
    public ResponseEntity<SendPostcardResponse> sendPostcard(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid SendPostcardRequest sendPostcardRequest
    ) {
        Long memberId = loginUser.getMemberId();
        SendPostcardResponse sendSearchResponses = postcardService.send(memberId,
                sendPostcardRequest);
        return ResponseEntity.ok(sendSearchResponses);
    }

    @PostMapping("/send/validation")
    public ResponseEntity<PostcardSendValidateResponse> sendPostcard(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody PostcardSendValidationRequest request
    ) {
        PostcardSendValidateResponse response = postcardService.validateSendPostcard(
                loginUser.getMemberId(), request.getTargetMemberId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type-list")
    public ResponseEntity<PostcardTypeResponse> getPostcardTypeList(
    ) {
        PostcardTypeResponse postcardTypeResponse = postcardService.getPostcardTypeList();
        return ResponseEntity.ok(postcardTypeResponse);
    }
}
