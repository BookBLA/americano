package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;

public interface MemberProfileService {

    MemberProfileResponse createMemberProfile(Long memberId,
        MemberProfileCreateRequest memberProfileCreateRequest);

    MemberProfileResponse readMemberProfile(Long memberId);

    MemberProfileResponse updateMemberProfile(Long memberId,
        MemberProfileUpdateRequest memberProfileUpdateRequest);


}
