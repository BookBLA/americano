package com.bookbla.americano.domain.auth.service;

import com.bookbla.americano.domain.auth.controller.dto.request.LoginRequest;
import com.bookbla.americano.domain.auth.controller.dto.response.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest loginRequest, String oAuthType);
}
