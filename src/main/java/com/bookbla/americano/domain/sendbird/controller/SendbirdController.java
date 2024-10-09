package com.bookbla.americano.domain.sendbird.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.sendbird.controller.dto.SendbirdResponse;
import com.bookbla.americano.domain.sendbird.service.SendbirdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sendbird.client.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name="Sendbird" , description = "Sendbird API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/sendbird")
public class SendbirdController {

    private final SendbirdService sendbirdService;

    @Operation(summary = "Sendbird 유저 생성 및 유저 토큰 생성/저장")
    @PostMapping
    public ResponseEntity<SendbirdResponse> sendbird(@Parameter(hidden = true) @User LoginUser loginUser) throws ApiException {
        sendbirdService.createUser(loginUser.getMemberId());
        SendbirdResponse sendbirdToken = sendbirdService.createUserToken(loginUser.getMemberId());
        return ResponseEntity.ok(sendbirdToken);
    }
}
