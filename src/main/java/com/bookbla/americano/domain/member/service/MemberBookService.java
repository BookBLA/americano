package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberBookCreateRequest;

public interface MemberBookService {

    Long addMemberBook(Long memberId, MemberBookCreateRequest memberBookCreateRequest);

}
