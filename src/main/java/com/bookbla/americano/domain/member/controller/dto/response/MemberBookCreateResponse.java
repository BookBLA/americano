package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberBookCreateResponse {

    private final Long memberBookId;

    public static MemberBookCreateResponse from(MemberBook memberBook) {
        return MemberBookCreateResponse.builder()
                .memberBookId(memberBook.getId())
                .build();
    }
}
