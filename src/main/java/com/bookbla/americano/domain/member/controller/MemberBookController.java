package com.bookbla.americano.domain.member.controller;

import com.bookbla.americano.base.jwt.LoginUser;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookCreateRequest;
import com.bookbla.americano.domain.member.service.MemberBookService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Void> addMemberBook(
            @LoginUser Long memberId,
            @RequestBody @Valid MemberBookCreateRequest memberBookCreateRequest
    ) {
        Long memberBookId = memberBookService.addMemberBook(memberId, memberBookCreateRequest);
        return ResponseEntity.created(URI.create("/member-books/" + memberBookId.toString())).build();
    }

}
