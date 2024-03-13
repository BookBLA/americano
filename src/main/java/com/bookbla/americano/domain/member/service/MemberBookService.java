package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberBookCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberBookReadResponses;

public interface MemberBookService {

    Long addMemberBook(Long memberId, MemberBookCreateRequest memberBookCreateRequest);

    MemberBookReadResponses readMemberBooks(Long memberId);

}
