package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.request.MemberContactCreateRequest;
import com.bookbla.americano.domain.member.controller.dto.response.MemberContactCreateResponse;
import com.bookbla.americano.domain.member.controller.dto.response.MemberContactReadResponse;

public interface MemberContactService {

    MemberContactCreateResponse createMemberContacts(Long memberId,
        MemberContactCreateRequest memberContactCreateRequest);

    MemberContactReadResponse readMemberContacts(Long memberId);
}
