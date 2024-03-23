package com.bookbla.americano.domain.auth.service;

import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequestDto;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto loginRequestDto, String oAuthType);

}
