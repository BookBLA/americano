package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberBookCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.request.MemberBookUpdateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponses;

public interface MemberBookService {

    MemberBookCreateResponse addMemberBook(Long memberId, MemberBookCreateRequest memberBookCreateRequest);

    MemberBookReadResponses readMemberBooks(Long memberId);

    MemberBookReadResponse readMemberBook(Long memberBookId);

    void deleteMemberBook(Long memberId, Long memberBookId);

    void updateMemberBook(MemberBookUpdateRequest memberBookUpdateRequest, Long memberBookId, Long memberId);
}
