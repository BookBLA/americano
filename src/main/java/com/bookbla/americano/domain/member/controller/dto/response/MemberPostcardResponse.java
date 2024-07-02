package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberPostcard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberPostcardResponse {

    private final Long memberPostcardId;

    public static MemberPostcardResponse from(MemberPostcard memberPostcard) {
        return new MemberPostcardResponse(memberPostcard.getId());
    }

    public static MemberPostcardResponse none() {
        return new MemberPostcardResponse(-1L);
    }
}
