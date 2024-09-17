package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.school.repository.entity.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInvitationRewardResponse {

    private Boolean inviting;
    private Boolean invited;

    public static MemberInvitationRewardResponse from(Boolean inviting, Boolean invited) {
        return new MemberInvitationRewardResponse(inviting, invited);
    }
}
