package com.bookbla.americano.domain.postcard.controller;

import com.bookbla.americano.base.resolver.LoginUser;
import com.bookbla.americano.base.resolver.User;
import com.bookbla.americano.domain.book.service.dto.BookSearchResponses;
import com.bookbla.americano.domain.postcard.service.PostcardService;
import com.bookbla.americano.domain.postcard.service.dto.request.SendPostcardRequest;
import com.bookbla.americano.domain.postcard.service.dto.response.PostcardTypeResponse;
import com.bookbla.americano.domain.postcard.service.dto.response.SendPostcardResponse;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/postcard")
@RequiredArgsConstructor
public class PostcardController {

    private final PostcardService postcardService;

    @PostMapping("/send")
    public ResponseEntity<SendPostcardResponse> sendPostcard(
            @Parameter(hidden = true) @User LoginUser loginUser,
            @RequestBody @Valid SendPostcardRequest sendPostcardRequest
    ) {
        Long memberId = loginUser.getMemberId();
        SendPostcardResponse sendSearchResponses = postcardService.send(memberId, sendPostcardRequest);
        return ResponseEntity.ok(sendSearchResponses);
    }

    @GetMapping("/type-list")
    public ResponseEntity<PostcardTypeResponse> getPostcardTypeList(
    ) {
        PostcardTypeResponse postcardTypeResponse = postcardService.getPostcardTypeList();
        return ResponseEntity.ok(postcardTypeResponse);
    }
}