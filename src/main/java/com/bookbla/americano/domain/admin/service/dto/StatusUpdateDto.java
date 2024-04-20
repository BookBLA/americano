package com.bookbla.americano.domain.admin.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StatusUpdateDto {

    private final Long memberId;
    private final String status;

}
