package com.bookbla.americano.domain.auth.presntation;

import com.bookbla.americano.domain.auth.domain.JwtTokenProvider;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public String login() {
        // 대충 검증로직
        return jwtTokenProvider.createToken("암호화 해서 보낼 사용자 구분자");
    }

}
