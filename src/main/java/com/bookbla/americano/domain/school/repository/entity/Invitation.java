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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long invitingMemberId;

    private Long invitedMemberId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private InvitationStatus invitationStatus = PENDING;

    public void bookmark() {
        this.invitationStatus = BOOKMARK;
    }

    public void complete() {
        this.invitationStatus = COMPLETED;
    }
}

