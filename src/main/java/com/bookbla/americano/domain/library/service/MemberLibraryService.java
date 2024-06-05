package com.bookbla.americano.domain.library.service;

import com.bookbla.americano.domain.library.controller.dto.MemberLibraryProfileReadResponse;
import com.bookbla.americano.domain.library.controller.dto.MemberTargetLibraryProfileReadResponse;

public interface MemberLibraryService {

    MemberLibraryProfileReadResponse getLibraryProfile(Long memberId);

    MemberTargetLibraryProfileReadResponse getTargetLibraryProfile(Long memberId, Long targetMemberId);
}
