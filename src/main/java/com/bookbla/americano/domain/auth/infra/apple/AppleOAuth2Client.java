package com.bookbla.americano.domain.auth.infra.apple;

import com.bookbla.americano.domain.auth.infra.apple.dto.AppleTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "apple", url = "https://appleid.apple.com")
public interface AppleOAuth2Client {

    @PostMapping("/auth/token")
    AppleTokenResponse getToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("grant_type") String grantType,
            @RequestParam("code") String code
    );

}
