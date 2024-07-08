package com.bookbla.americano.domain.auth.service.dto.event;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AdminNotificationEventWithoutTransaction {

    private String title;
    private String contents;

}
