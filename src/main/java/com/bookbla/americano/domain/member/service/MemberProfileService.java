package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;

public interface MemberProfileService {

    void createMemberProfile(Long memberId, MemberProfileDto memberProfileDto);

    MemberProfileResponse readMemberProfile(Long memberId);

    MemberProfileResponse updateMemberProfile(Long memberId,
        MemberProfileUpdateRequest memberProfileUpdateRequest);

}
