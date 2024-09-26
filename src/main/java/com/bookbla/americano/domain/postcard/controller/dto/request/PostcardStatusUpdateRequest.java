package com.bookbla.americano.domain.postcard.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PostcardStatusUpdateRequest {

    @NotNull(message = "postcardId를 입력하지 않았습니다.")
    private Long postcardId;

    @NotNull(message = "status를 입력하지 않았습니다.")
    private String status;

    @Nullable
    private Long memberBookId;
}
