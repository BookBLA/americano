package com.bookbla.americano.domain.memberask.service;

import com.bookbla.americano.domain.memberask.controller.dto.request.MemberAskCreateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.request.MemberAskUpdateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.response.MemberAskResponse;

public interface MemberAskService {

    MemberAskResponse createMemberAsk(Long memberId, MemberAskCreateRequest memberAskCreateRequest);


    MemberAskResponse readMemberAsk(Long memberId);

    void updateMemberAsk(Long memberId, MemberAskUpdateRequest memberAskUpdateRequest);
}
