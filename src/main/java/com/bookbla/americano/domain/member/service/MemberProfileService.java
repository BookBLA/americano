package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.MemberCreateResponse;
import com.bookbla.americano.domain.member.service.dto.MemberDto;

public interface MemberProfileService {

    MemberCreateResponse createProfile(MemberDto memberDto);

}
