package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.MemberLibraryProfileReadResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberTargetLibraryProfileReadResponse;

public interface MemberLibraryService {

    MemberLibraryProfileReadResponse getLibraryProfile(Long memberId);

    MemberTargetLibraryProfileReadResponse getTargetLibraryProfile(Long memberId, Long targetMemberId);
}
