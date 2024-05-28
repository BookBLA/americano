package com.bookbla.americano.domain.auth.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LoginRequest {

    @NotNull(message = "인가코드가 입력되지 않았습니다")
    private String authCode;

}
