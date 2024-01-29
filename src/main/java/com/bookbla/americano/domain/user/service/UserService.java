package com.bookbla.americano.domain.user.service;


import com.bookbla.americano.domain.user.controller.dto.request.UserRegisterRequestDto;
import com.bookbla.americano.domain.user.controller.dto.response.UserRegisterResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    @Transactional
    UserRegisterResponseDto signup(UserRegisterRequestDto requestDTO);

    @Transactional(readOnly = true)
    UserRegisterResponseDto getUserWithAuthorities(String email);

    @Transactional(readOnly = true)
    UserRegisterResponseDto getMyUserWithAuthorities();

    @Transactional(readOnly = true)
    boolean isUser(String email);
}
