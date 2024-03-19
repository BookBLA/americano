package com.bookbla.americano.domain.member.service;


import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberStyleUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberStyleResponse;

public interface MemberStyleService {

    MemberStyleResponse readMemberStyle(Long memberId);

    MemberStyleResponse readMemberStyle(Long memberId, Long targetMemberId);

    MemberStyleResponse createMemberStyle(Long memberId, MemberStyleCreateRequest memberStyleCreateRequest);

    void updateMemberStyle(Long memberId, MemberStyleUpdateRequest memberStyleUpdateRequest);

}
