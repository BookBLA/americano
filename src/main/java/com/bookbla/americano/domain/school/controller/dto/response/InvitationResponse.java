package com.bookbla.americano.domain.school.controller.dto.response;

import com.bookbla.americano.domain.school.repository.entity.Invitation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InvitationResponse {

    private final String invitationType;

    public static InvitationResponse from(Invitation invitation) {
        return new InvitationResponse(invitation.getInvitationType().name());
    }
}
