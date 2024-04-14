package com.bookbla.americano.domain.postcard.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.book.service.dto.BookSearchResponses;
import com.bookbla.americano.domain.postcard.service.PostcardService;
import com.bookbla.americano.domain.postcard.service.dto.request.SendPostcardRequest;
import com.bookbla.americano.domain.postcard.service.dto.response.SendPostcardResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/postcard")
@RequiredArgsConstructor
public class PostcardController {

    private final PostcardService postcardService;

    @PostMapping("/send")
    public ResponseEntity<SendPostcardResponse> searchBooks(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody SendPostcardRequest sendPostcardRequest
    ) {
        Long memberId = loginUser.getMemberId();
        SendPostcardResponse sendSearchResponses = postcardService.send(memberId, sendPostcardRequest);
        return ResponseEntity.ok(sendSearchResponses);
    }
}
