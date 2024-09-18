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
    private Map<Long, Boolean> inviting = new HashMap<>(); // key: invitedMemberId, value: rewarded

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private InvitationStatus invited = InvitationStatus.PENDING;

    public void updateMemberHomeOnboarding() {
        this.homeOnboarding = Boolean.TRUE;
    }

    public void updateMemberLibraryOnboarding() {
        this.libraryOnboarding = Boolean.TRUE;
    }

    public InvitationStatus updateMemberInvited(InvitationStatus invitationStatus) {
        this.invited = invitationStatus;
        return this.invited;
    }
}