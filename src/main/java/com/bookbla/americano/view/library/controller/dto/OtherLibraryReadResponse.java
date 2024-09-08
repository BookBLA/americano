package com.bookbla.americano.view.library.controller.dto;

import java.util.List;

import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberBook;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OtherLibraryReadResponse {

    private final MyLibraryReadResponse baseResponse;
    private final boolean isMatched;

    public static OtherLibraryReadResponse of(Member member, List<MemberBook> memberBooks, boolean isMatched) {
        MyLibraryReadResponse baseResponse = MyLibraryReadResponse.of(member, memberBooks);
        return new OtherLibraryReadResponse(baseResponse, isMatched);
    }
}

