package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.MemberCreateResponse;
import com.bookbla.americano.domain.member.service.dto.MemberDto;
import org.springframework.transaction.annotation.Transactional;

public interface MemberAuthService {

    MemberCreateResponse createAuth(MemberDto memberDto);
}
