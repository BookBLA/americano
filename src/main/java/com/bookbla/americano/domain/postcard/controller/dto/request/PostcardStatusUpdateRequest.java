package com.bookbla.americano.domain.postcard.controller.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PostcardStatusUpdateRequest {
    @NotNull
    private Long postcardId;
    @NotNull
    private String status;
}
