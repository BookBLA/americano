package com.bookbla.americano.domain.admin.controller.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Getter
public class AdminLogoutRequest {

    @NotBlank(message = "세션 아이디가 입력되지 않았습니다.")
    private String sessionId;

}
