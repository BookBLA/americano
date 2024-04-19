package com.bookbla.americano.domain.auth.service;

import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequestDto;
import com.bookbla.americano.domain.auth.controller.dto.request.RejoinRequest;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponseDto;
import com.bookbla.americano.domain.auth.controller.dto.response.RejoinResponse;

public interface AuthService {

    LoginResponseDto login(LoginRequestDto loginRequestDto, String oAuthType);

    RejoinResponse rejoin(RejoinRequest rejoinRequest, String oAuthType);
}
