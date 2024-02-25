package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileCreateResponse;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;

public interface MemberProfileService {

    MemberProfileCreateResponse createProfile(MemberProfileDto memberProfileDto);

}
