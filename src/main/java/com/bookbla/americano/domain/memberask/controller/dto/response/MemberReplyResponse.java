package com.bookbla.americano.domain.memberask.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberReplyResponse {
    private final Long memberReplyId;
    private final String askContent;
    private final String replyContent;
}
