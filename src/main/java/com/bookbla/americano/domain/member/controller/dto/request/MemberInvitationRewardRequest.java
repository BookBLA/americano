package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.school.repository.entity.InvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class MemberInvitationRewardRequest {

    @NotNull
    private InvitationStatus invitationStatus;
}
