package com.bookbla.americano.domain.memberask.service;

import com.bookbla.americano.domain.memberask.controller.dto.request.MemberReplyUpdateRequest;
import com.bookbla.americano.domain.memberask.controller.dto.response.MemberReplyResponse;

public interface MemberReplyService {
    MemberReplyResponse readMemberReply(Long memberId, Long postcardId);

    void updateMemberReply(Long memberId, MemberReplyUpdateRequest memberReplyUpdateRequest);
}
