package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileStatusUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponseDto;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileStatusResponse;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;

import java.util.List;

public interface MemberProfileService {

    List<MemberBookProfileResponseDto> findSameBookMembers(Long memberId, MemberBookProfileRequestDto memberBookProfileRequestDto);
    MemberProfileResponse createMemberProfile(Long memberId, MemberProfileDto memberProfileDto);
    MemberProfileResponse readMemberProfile(Long memberId);
    MemberProfileResponse updateMemberProfile(Long memberId,
        MemberProfileUpdateRequest memberProfileUpdateRequest);

    MemberProfileStatusResponse readMemberProfileStatus(Long memberId);

    MemberProfileStatusResponse updateMemberProfileStatus(Long memberId,
        MemberProfileStatusUpdateRequest memberProfileStatusUpdateRequest);

}
