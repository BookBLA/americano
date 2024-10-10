package com.bookbla.americano.domain.sendbird.controller.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class ChatRequest {

    @NotNull(message = "채팅을 수락할 회원이 입력 되지 않았습니다")
    private Long targetMemberId;
}