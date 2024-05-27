package com.bookbla.americano.domain.member.service;

import java.util.List;

import com.bookbla.americano.domain.member.controller.dto.request.MemberBookProfileRequestDto;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileImageUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileOpenKakaoRoomUrlUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberProfileUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberProfileStatusResponse;
import com.bookbla.americano.domain.member.service.dto.MemberProfileDto;
import com.bookbla.americano.domain.member.service.dto.MemberProfileStatusDto;

public interface MemberProfileService {

    List<MemberBookProfileResponse> findSameBookMembers(Long memberId, MemberBookProfileRequestDto memberBookProfileRequestDto);

    List<MemberBookProfileResponse> getAllMembers(Long memberId, MemberBookProfileRequestDto requestDto);

    MemberProfileResponse createMemberProfile(Long memberId, MemberProfileDto memberProfileDto);

    MemberProfileResponse readMemberProfile(Long memberId);

    MemberProfileResponse updateMemberProfile(Long memberId, MemberProfileUpdateRequest memberProfileUpdateRequest);

    MemberProfileStatusResponse readMemberProfileStatus(Long memberId);

    MemberProfileStatusResponse updateMemberProfileStatus(Long memberId,
                                                          MemberProfileStatusDto memberProfileStatusDto);

    void updateMemberProfileImage(Long memberId, MemberProfileImageUpdateRequest request);

    void updateMemberProfileKakaoRoom(Long memberId, MemberProfileOpenKakaoRoomUrlUpdateRequest request);

}
