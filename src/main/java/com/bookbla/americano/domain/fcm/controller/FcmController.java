package com.bookbla.americano.domain.fcm.controller;

import com.bookbla.americano.domain.fcm.controller.dto.request.FcmRequest;
import com.bookbla.americano.domain.fcm.controller.dto.request.FcmTokenCreateRequest;
import com.bookbla.americano.domain.fcm.controller.dto.response.FcmResponse;
import com.bookbla.americano.domain.fcm.controller.dto.response.FcmTokenCreateResponse;
import com.bookbla.americano.domain.fcm.controller.dto.response.FcmTokenDeleteResponse;
import com.bookbla.americano.domain.fcm.service.FcmService;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fcm")
@Slf4j
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/tokens")
    public ResponseEntity<FcmTokenCreateResponse> saveFcmToken(@RequestParam Long memberId,
        @RequestBody @Valid FcmTokenCreateRequest fcmTokenCreateRequest) {

        FcmTokenCreateResponse fcmTokenCreateResponse = fcmService.saveFcmToken(memberId,
            fcmTokenCreateRequest);

        return ResponseEntity.ok(fcmTokenCreateResponse);
    }

    @DeleteMapping("/tokens")
    public ResponseEntity<FcmTokenDeleteResponse> deleteFcmToken(@RequestParam Long memberId) {

        FcmTokenDeleteResponse fcmTokenDeleteResponse = fcmService.deleteFcmToken(memberId);

        return ResponseEntity.ok(fcmTokenDeleteResponse);
    }

    @PostMapping("/pushes")
    public ResponseEntity<FcmResponse> pushMessage(@RequestBody @Valid FcmRequest fcmRequest)
        throws IOException {
        log.info("targetToken : " + fcmRequest.getTargetToken());
        log.info("title : " + fcmRequest.getTitle());
        log.info("body : " + fcmRequest.getBody());

        fcmService.sendMessageTo(
            fcmRequest.getTargetToken(),
            fcmRequest.getTitle(),
            fcmRequest.getBody()
        );

        return ResponseEntity.ok(FcmResponse.builder()
            .title("제목")
            .contents("내용")
            .build());
    }
}
