package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInvitationRewardResponse {

    private Boolean invitingRewardStatus;
    private String invitedRewardStatus; // 초대코드 입력 안함 -> NONE, 다른 회원 초대코드 입력 -> MEMBER, 축제 코드 입력 -> FESTIVAL, 이벤트 코드 입력 -> EVENT
    private String invitedMembersGender;

    public static MemberInvitationRewardResponse fromInvitingRewardNotGiven(String invitedRewardStatus) {
        return new MemberInvitationRewardResponse(false, invitedRewardStatus, null);
    }

    public static MemberInvitationRewardResponse fromInvitingRewardGiven(String invitedRewardStatus, Gender invitedMembersGender) {
        return new MemberInvitationRewardResponse(true, invitedRewardStatus, invitedMembersGender.name());
    }
}
