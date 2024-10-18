package com.bookbla.americano.domain.sendbird.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class EntryRequest {

    @NotNull(message = "채팅 수락/거절시 책갈피 처리할 회원이 입력 되지 않았습니다")
    private Long targetMemberId;

    @NotNull(message = "postcardId를 입력하지 않았습니다.")
    private Long postcardId;

    private String channelUrl;
}