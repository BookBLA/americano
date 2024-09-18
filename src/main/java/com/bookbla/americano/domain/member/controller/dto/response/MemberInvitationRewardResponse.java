package com.bookbla.americano.domain.member.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInvitationRewardResponse {

    private Boolean invitingRewardStatus;
    private Boolean invitedRewardStatus;

    public static MemberInvitationRewardResponse from(Boolean invitingRewardStatus, Boolean invitedRewardStatus) {
        return new MemberInvitationRewardResponse(invitingRewardStatus, invitedRewardStatus);
    }
}
