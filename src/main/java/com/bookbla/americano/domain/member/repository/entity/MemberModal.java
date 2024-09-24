package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.school.exception.InvitationExceptionType;
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
//    @CollectionTable(name = "member_inviting", joinColumns = @JoinColumn(name = "member_id"))
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

    public boolean isInvitedRewardNotGiven() {
        return invitedRewardStatus == InvitationStatus.BOOKMARK;
    }

    public boolean isInvitingRewardNotGiven() {
        return invitingRewardStatus.entrySet().stream().anyMatch(entry -> entry.getValue().equals(Boolean.FALSE));
    }

    public void updateMemberInvitedRewardStatusToBookmark() {
        this.invitedRewardStatus = InvitationStatus.BOOKMARK;
    }

    public void updateMemberInvitedRewardStatusToComplete() {
        this.invitedRewardStatus = InvitationStatus.COMPLETED;
    }

    public Long getInvitedMemberId() {
        return invitingRewardStatus.entrySet().stream()
                .filter(entry -> entry.getValue().equals(Boolean.FALSE))
                .findFirst()
                .map(entry -> {
                    entry.setValue(Boolean.TRUE);
                    return entry.getKey();
                })
                .orElseThrow(() -> new BaseException(InvitationExceptionType.NO_INVITED_MEMBER));
    }
}