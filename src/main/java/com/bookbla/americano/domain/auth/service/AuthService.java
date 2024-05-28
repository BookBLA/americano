package com.bookbla.americano.domain.auth.service;

import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequest;
import com.bookbla.americano.domain.auth.controller.dto.request.RejoinRequest;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponse;
import com.bookbla.americano.domain.auth.controller.dto.response.RejoinResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest, String oAuthType);

    RejoinResponse rejoin(RejoinRequest rejoinRequest, String oAuthType);
}
