package com.bookbla.americano.domain.postcard.service.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SendPostcardRequest {

    @NotNull(message = "postcardTypeId가 입력되지 않았습니다.")
    private Long postcardTypeId;

    @NotNull(message = "엽서를 보낼 상대방의 식별자가 입력되지 않았습니다")
    private Long receiveMemberId;

    @NotNull(message = "엽서를 보낼 상대방의 책 식별자가 입력되지 않았습니다")
    private Long receiveMemberBookId;

    @NotNull(message = "memberReply가 입력되지 않았습니다.")
    @Size(max = 150)
    private String memberReply;

    @NotNull(message = "channelUrl이 입력되지않았습니다.")
    private String channelUrl;
}