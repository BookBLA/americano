package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberBlockCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBlockCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBlockDeleteResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBlockReadResponse;

public interface MemberBlockService {

    MemberBlockCreateResponse addMemberBlock(Long memberId, MemberBlockCreateRequest memberBlockCreateRequest);

    MemberBlockReadResponse readMemberBlock(Long memberId);

    MemberBlockDeleteResponse deleteMemberBlock(Long memberId, Long memberBlockId);
}
