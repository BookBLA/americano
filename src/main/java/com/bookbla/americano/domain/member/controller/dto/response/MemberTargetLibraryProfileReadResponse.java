package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import com.bookbla.americano.domain.member.repository.entity.MemberProfile;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberTargetLibraryProfileReadResponse {
    private final MemberLibraryProfileReadResponse baseResponse;
    private final boolean isMatched;

    public static MemberTargetLibraryProfileReadResponse of(Member member, MemberProfile memberProfile, List<MemberBook> memberBooks, boolean isMatched) {
        MemberLibraryProfileReadResponse baseResponse = MemberLibraryProfileReadResponse.of(member, memberProfile, memberBooks);
        return new MemberTargetLibraryProfileReadResponse(baseResponse, isMatched);
    }
}

