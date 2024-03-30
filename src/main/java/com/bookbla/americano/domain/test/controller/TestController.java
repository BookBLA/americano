package com.bookbla.americano.domain.test.controller;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.BaseExceptionType;
import com.bookbla.americano.base.jwt.JwtProvider;
import com.bookbla.americano.domain.auth.service.AuthService;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.test.controller.dto.request.TestCreateRequest;
import com.bookbla.americano.domain.test.controller.dto.request.TestSignUpRequest;
import com.bookbla.americano.domain.test.controller.dto.response.TestCreateResponse;
import com.bookbla.americano.domain.test.controller.dto.response.TestReadResponse;
import com.bookbla.americano.domain.test.controller.dto.response.TestSignUpResponse;
import com.bookbla.americano.domain.test.service.TestService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    private final JwtProvider jwtProvider;


    @GetMapping
    public ResponseEntity<List<TestReadResponse>> readTest(@RequestParam String contents) {
        List<TestReadResponse> readTestResponses = testService.findTestsByContents(contents);
        return ResponseEntity.ok(readTestResponses);
    }

    @PostMapping
    public ResponseEntity<TestCreateResponse> createTest(@RequestBody @Valid TestCreateRequest testCreateRequest) {
        TestCreateResponse testCreateResponse = testService.create(testCreateRequest.toDto());
        return ResponseEntity.created(URI.create("/tests/" + testCreateResponse.getId()))
                .body(testCreateResponse);
    }

    @GetMapping("/error")
    public void testError() {
        throw new BaseException(BaseExceptionType.TEST_FAIL);
    }


    @PostMapping("/sign-up")
    public ResponseEntity<TestSignUpResponse> testSignUp(
            @RequestBody TestSignUpRequest testSignUpRequest
    ) {
        Member member = testService.signUp(testSignUpRequest.getEmail());
        String token = jwtProvider.createToken(member.getId().toString());
        return ResponseEntity.ok(TestSignUpResponse.of(member, token));
    }
}
