package com.bookbla.americano.domain.library.controller.dto;

import java.util.List;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberTargetLibraryProfileReadResponse {

    private final MemberLibraryProfileReadResponse baseResponse;
    private final boolean isMatched;

    public static MemberTargetLibraryProfileReadResponse of(Member member, List<MemberBook> memberBooks, boolean isMatched) {
        MemberLibraryProfileReadResponse baseResponse = MemberLibraryProfileReadResponse.of(member, memberBooks);
        return new MemberTargetLibraryProfileReadResponse(baseResponse, isMatched);
    }
}

