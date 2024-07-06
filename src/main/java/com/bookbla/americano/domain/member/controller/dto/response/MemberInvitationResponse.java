package com.bookbla.americano.domain.member.controller.dto.response;


import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MemberInvitationResponse {

    private final String invitationCode;

    public static MemberInvitationResponse from(Member member) {
        return new MemberInvitationResponse(member.getInvitationCode());
    }
}
