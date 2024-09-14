package com.bookbla.americano.domain.auth.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class KakaoLoginRequest {

    @NotNull(message = "카카오 인증 액세스 토큰이 입력되지 않았습니다")
    private String accessToken;
}
