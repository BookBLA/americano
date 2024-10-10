package com.bookbla.americano.domain.test.controller;

import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.test.controller.docs.TestControllerDocs;
import com.bookbla.americano.domain.test.controller.dto.request.TestLoginRequest;
import com.bookbla.americano.domain.test.controller.dto.request.TestSignUpRequest;
import com.bookbla.americano.domain.test.controller.dto.response.TestSignUpResponse;
import com.bookbla.americano.domain.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestController implements TestControllerDocs {

    private final TestService testService;
    private final JwtProvider jwtProvider;

    @PostMapping("/sign-up/admin")
    public ResponseEntity<TestSignUpResponse> testSignUpAdmin(
            @RequestBody TestSignUpRequest testSignUpRequest
    ) {
        Member member = testService.signUpAdmin(testSignUpRequest.getEmail());
        String token = jwtProvider.createToken(member.getId().toString());
        return ResponseEntity.ok(TestSignUpResponse.of(member.getId(), token));
    }

    @PostMapping("/sign-up/kakao")
    public ResponseEntity<TestSignUpResponse> testSignUpKakao(
            @RequestBody TestSignUpRequest testSignUpRequest
    ) {
        Member member = testService.signUpKakao(testSignUpRequest.getEmail());
        String token = jwtProvider.createToken(member.getId().toString());
        return ResponseEntity.ok(TestSignUpResponse.of(member.getId(), token));
    }

    @PostMapping("/sign-up/script")
    public ResponseEntity<String> testLoginScript(
            @RequestBody TestLoginRequest testLoginRequest
    ) {
        Member member = testService.loginScript(testLoginRequest.getId());
        String token = jwtProvider.createToken(member.getId().toString());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> testDataReset(
            @RequestBody TestLoginRequest testLoginRequest
    ) {
        testService.dataReset(testLoginRequest.getId());
        return ResponseEntity.ok().build();
    }
}
