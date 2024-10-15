package com.bookbla.americano.domain.sendbird.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.sendbird.controller.dto.request.EntryRequest;
import com.bookbla.americano.domain.sendbird.controller.dto.response.SendbirdResponse;
import com.bookbla.americano.domain.sendbird.service.ChatService;
import com.bookbla.americano.domain.sendbird.service.SendbirdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name="Sendbird" , description = "Sendbird API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sendbird")
public class SendbirdController {

    private final SendbirdService sendbirdService;
    private final ChatService chatService;

    @Operation(summary = "Sendbird 유저 생성 및 유저 토큰 생성/저장 or 유저 조회")
    @PostMapping
    public ResponseEntity<SendbirdResponse> sendbird(@Parameter(hidden = true) @User LoginUser loginUser) {
        SendbirdResponse sendbirdResponse = sendbirdService.createOrView(loginUser.getMemberId());
        return ResponseEntity.ok(sendbirdResponse);
    }

    @Operation(summary = "채팅 수락")
    @PostMapping("/entry/accept")
    public ResponseEntity<Void> accept(@RequestBody @Valid EntryRequest entryRequest){
        chatService.chatAccept(entryRequest.getTargetMemberId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "채팅 거절")
    @PostMapping("/entry/reject")
    public ResponseEntity<Void> reject(@RequestBody @Valid EntryRequest entryRequest){
        chatService.chatReject(entryRequest.getTargetMemberId());
        return ResponseEntity.ok().build();
    }
}
