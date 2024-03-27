package com.bookbla.americano.domain.admin.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Getter
public class AdminLoginRequest {

    @NotBlank(message = "아이디가 입력되지 않았습니다.")
    private String id;

    @NotBlank(message = "비밀번호가 입력되지 않았습니다.")
    private String password;

}
