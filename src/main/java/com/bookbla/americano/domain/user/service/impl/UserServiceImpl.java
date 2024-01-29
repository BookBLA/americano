package com.bookbla.americano.domain.user.service.impl;

import com.bookbla.americano.base.enums.UserRoleEnum;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.exception.UserErrorType;
import com.bookbla.americano.base.util.SecurityUtil;
import com.bookbla.americano.domain.auth.repository.AuthorityRepository;
import com.bookbla.americano.domain.user.controller.dto.request.UserRegisterRequestDto;
import com.bookbla.americano.domain.user.controller.dto.response.UserRegisterResponseDto;
import com.bookbla.americano.domain.user.repository.UserRepository;
import com.bookbla.americano.domain.user.repository.entity.Authority;
import com.bookbla.americano.domain.user.repository.entity.User;
import com.bookbla.americano.domain.user.repository.entity.UserAuthority;
import com.bookbla.americano.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;


    public UserRegisterResponseDto signup(UserRegisterRequestDto userRegisterRequestDto) {
        Authority authority = authorityRepository.findByAuthorityName(UserRoleEnum.ROLE_USER.name());

        User user = User.builder()
                .email(userRegisterRequestDto.getEmail())
                .password(passwordEncoder.encode(userRegisterRequestDto.getPassword()))
                .nickname(userRegisterRequestDto.getNickname())
                .sex(userRegisterRequestDto.getSex())
                .birthday(userRegisterRequestDto.getBirthday())
                .build();

        user.setUserAuthoritySet(
                Collections.singleton(UserAuthority.builder()
                        .user(user)
                        .authority(authority)
                        .build())
        );

        return UserRegisterResponseDto.from(userRepository.save(user));
    }

    @Override
    public UserRegisterResponseDto getUserWithAuthorities(String email) {
        return UserRegisterResponseDto.from(userRepository.findOneWithUserAuthorityByEmail(email).orElse(null));
    }

    @Override
    public UserRegisterResponseDto getMyUserWithAuthorities() {
        return UserRegisterResponseDto.from(
                SecurityUtil.getCurrentUserEmail()
                        .flatMap(userRepository::findOneWithUserAuthorityByEmail)
                        .orElseThrow(() -> new BaseException(UserErrorType.USER_NOT_FOUND))
        );
    }

    @Override
    public boolean isUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
