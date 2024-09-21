package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInvitationRewardResponse {

    private Boolean invitingRewardStatus;
    private Boolean invitedRewardStatus;
    private Gender invitedMembersGender;

    public static MemberInvitationRewardResponse from(Boolean invitingRewardStatus, Boolean invitedRewardStatus) {
        return new MemberInvitationRewardResponse(invitingRewardStatus, invitedRewardStatus, null);
    }

    public static MemberInvitationRewardResponse from(Boolean invitingRewardStatus, Boolean invitedRewardStatus, Gender invitedMembersGender) {
        return new MemberInvitationRewardResponse(invitingRewardStatus, invitedRewardStatus, invitedMembersGender);
    }
}
