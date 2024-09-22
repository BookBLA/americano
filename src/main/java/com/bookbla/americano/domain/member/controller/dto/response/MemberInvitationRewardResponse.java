package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInvitationRewardResponse {

    private Boolean invitingRewardStatus;
    private Boolean invitedRewardStatus;
    private String invitedMembersGender;

    public static MemberInvitationRewardResponse fromInvitingRewardNotGiven(Boolean invitedRewardStatus) {
        return new MemberInvitationRewardResponse(false, invitedRewardStatus, null);
    }

    public static MemberInvitationRewardResponse fromInvitingRewardGiven(Boolean invitedRewardStatus, Gender invitedMembersGender) {
        return new MemberInvitationRewardResponse(true, invitedRewardStatus, invitedMembersGender.name());
    }
}
