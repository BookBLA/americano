package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.domain.school.repository.entity.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Getter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberModal {

    @Builder.Default
    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean homeOnboarding = Boolean.FALSE;

    @Builder.Default
    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean libraryOnboarding = Boolean.FALSE;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "member_inviting", joinColumns = @JoinColumn(name = "member_id"))
    private Map<Long, Boolean> invitingRewardStatus = new HashMap<>(); // key: invitedMemberId, value: rewarded

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private InvitationStatus invitedRewardStatus = InvitationStatus.NONE;

    public void updateMemberHomeOnboarding() {
        this.homeOnboarding = Boolean.TRUE;
    }

    public void updateMemberLibraryOnboarding() {
        this.libraryOnboarding = Boolean.TRUE;
    }

    public InvitationStatus updateMemberInvitedRewardStatus(InvitationStatus invitationStatus) {
        this.invitedRewardStatus = invitationStatus;
        return this.invitedRewardStatus;
    }

    public Boolean getAndUpdateInvitingRewardStatus() {
        for (Map.Entry<Long, Boolean> entry : invitingRewardStatus.entrySet()) {
            if (!entry.getValue()) {
                entry.setValue(Boolean.TRUE);
                return true;
            }
        }
        return false;
    }

    public Boolean getAndUpdateInvitedRewardStatus() {
        if (invitedRewardStatus == InvitationStatus.BOOKMARK) {
            updateMemberInvitedRewardStatus(InvitationStatus.COMPLETED);
            return true;
        }
        return false;
    }
}