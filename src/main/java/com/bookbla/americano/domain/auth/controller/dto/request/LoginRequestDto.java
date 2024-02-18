package com.bookbla.americano.domain.auth.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class LoginRequestDto {

    @NotNull(message = "인가코드가 입력되지 않았습니다.")
    private String authCode;

}
