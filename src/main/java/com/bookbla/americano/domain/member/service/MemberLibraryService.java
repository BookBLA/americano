package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.MemberLibraryProfileReadResponse;

public interface MemberLibraryService {

    MemberLibraryProfileReadResponse getLibraryProfile(Long memberId);

}
