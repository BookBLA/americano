package com.bookbla.americano.domain.member.service.impl;

import com.bookbla.americano.domain.member.controller.dto.response.MemberCreateResponse;
import com.bookbla.americano.domain.member.repository.MemberAuthRepository;
import com.bookbla.americano.domain.member.repository.entity.MemberAuth;
import com.bookbla.americano.domain.member.service.MemberAuthService;
import com.bookbla.americano.domain.member.service.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberAuthServiceImpl implements MemberAuthService {

    private final MemberAuthRepository memberAuthRepository;

    @Override
    public MemberCreateResponse createAuth(MemberDto memberDto) {
        MemberAuth memberAuth = memberAuthRepository.save(memberDto.toMemberAuthEntity());
        return MemberCreateResponse.from(memberAuth);
    }

}
