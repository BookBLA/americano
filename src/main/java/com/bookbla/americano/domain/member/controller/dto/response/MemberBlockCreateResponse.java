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
    private Long blockedByMemberId;

    public static MemberBlockCreateResponse from(MemberBlock memberBlock, Member blocker, Member blocked) {
        return MemberBlockCreateResponse.builder()
            .memberBlockId(memberBlock.getId())
            .blockerMemberId(blocker.getId())
            .blockedByMemberId(blocked.getId())
            .build();
    }
}
