package com.bookbla.americano.domain.school.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InvitationResponse {

    private final String invitingMemberGender;

    public static InvitationResponse from(Member invitingMember) {
        return new InvitationResponse(
                invitingMember.getMemberProfile().getGenderName()
        );
    }

    public static InvitationResponse createFestivalTemporaryInvitationCode() {
        return new InvitationResponse("축제 임시 코드입니다");
    }
}
