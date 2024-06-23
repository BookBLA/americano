package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberBlockDeleteResponse {

    private Long memberBlockId;

    public static MemberBlockDeleteResponse from(Long memberBlockId){
        return MemberBlockDeleteResponse.builder()
            .memberBlockId(memberBlockId)
            .build();
    }
}
