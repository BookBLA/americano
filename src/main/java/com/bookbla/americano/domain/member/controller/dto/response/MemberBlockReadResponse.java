package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberBlock;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberBlockReadResponse {

    private final List<MemberBlockDetail> memberBlocks;

    @Getter
    @Builder
    public static class MemberBlockDetail {
        private final Long memberBlockId;
        private final Long blockedByMemberId;
    }

    public static MemberBlockReadResponse from(List<MemberBlock> memberBlocks) {
        List<MemberBlockDetail> memberBlockDetails = memberBlocks.stream()
            .map(memberBlock -> MemberBlockDetail.builder()
                .memberBlockId(memberBlock.getId())
                .blockedByMemberId(memberBlock.getBlockedByMember().getId())
                .build())
            .collect(Collectors.toList());

        return MemberBlockReadResponse.builder()
            .memberBlocks(memberBlockDetails)
            .build();
    }
}
