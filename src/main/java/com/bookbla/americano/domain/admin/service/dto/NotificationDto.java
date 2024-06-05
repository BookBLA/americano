package com.bookbla.americano.domain.admin.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NotificationDto {

    private final String title;
    private final String contents;

}
