package com.bookbla.americano.domain.postcard.controller.dto.response;

import com.bookbla.americano.domain.postcard.repository.entity.Postcard;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostcardReadResponse {

    private Long sendMemberId;

    private String sendMemberName;

    private Long receiveMemberId;

    private Long receiveMemberBookId;

    private  String memberReply;

    public static PostcardReadResponse of(Postcard postcard) {
        return new PostcardReadResponse(
                postcard.getSendMember().getId(),
                postcard.getSendMember().getMemberProfile().getName(),
                postcard.getReceiveMember().getId(),
                postcard.getReceiveMemberBook().getId(),
                postcard.getMessage());
    }
}
