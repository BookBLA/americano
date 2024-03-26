package com.bookbla.americano.domain.admin.controller.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AdminLoginResponse {

    private final String sessionId;

}
