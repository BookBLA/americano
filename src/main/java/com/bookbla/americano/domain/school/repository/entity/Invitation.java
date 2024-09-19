package com.bookbla.americano.domain.school.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.bookbla.americano.domain.school.repository.entity.InvitationStatus.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invitation extends BaseEntity {

    public static final long FESTIVAL_TEMPORARY_INVITING_MEMBER_ID = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long invitingMemberId;

    private Long invitedMemberId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus = BOOKMARK;

    @Enumerated(EnumType.STRING)
    private InvitationType invitationType;

    public static Invitation fromTempFestival(Long invitedMemberId) {
        return Invitation.builder()
                .invitedMemberId(invitedMemberId)
                .invitingMemberId(FESTIVAL_TEMPORARY_INVITING_MEMBER_ID)
                .invitationType(InvitationType.FESTIVAL)
                .build();
    }

    public void bookmark() {
        this.invitationStatus = BOOKMARK;
    }

    public void complete() {
        this.invitationStatus = COMPLETED;
    }

    public Invitation updateInvitationType(InvitationType invitationType) {
        this.invitationType = invitationType;
        return this;
    }

    public boolean isFestivalTemporaryInvitation() {
        return this.invitationType == InvitationType.FESTIVAL;
    }

    public boolean isWomanInvitation() {
        return this.invitationType == InvitationType.WOMAN;
    }

    public boolean isManInvitation() {
        return this.invitationType == InvitationType.MAN;
    }

    public boolean isComplete() {
        return this.invitationStatus == COMPLETED;
    }
}

