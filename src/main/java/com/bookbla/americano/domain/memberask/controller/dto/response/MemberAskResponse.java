package com.bookbla.americano.domain.memberask.controller.dto.response;

import com.bookbla.americano.domain.memberask.repository.entity.MemberAsk;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class MemberAskResponse {

    private final Long memberAskResponseId;
    private final String contents;

    public static MemberAskResponse from(MemberAsk memberAsk) {
        return new MemberAskResponse(memberAsk.getId(), memberAsk.getContents());
    }
}
