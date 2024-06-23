package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBlock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberBlockCreateResponse {

    private Long memberBlockId;
    private Long blockerMemberId;
    private Long blockedMemberId;

    public static MemberBlockCreateResponse from(MemberBlock memberBlock) {
        return MemberBlockCreateResponse.builder()
            .memberBlockId(memberBlock.getId())
            .blockerMemberId(memberBlock.getBlockerMember().getId())
            .blockedMemberId(memberBlock.getBlockedMember().getId())
            .build();
    }
}
