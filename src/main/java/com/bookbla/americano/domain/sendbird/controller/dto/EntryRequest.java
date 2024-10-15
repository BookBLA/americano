package com.bookbla.americano.domain.sendbird.controller.dto;

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
}