package com.bookbla.americano.domain.notification.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InAppAlarmRequest {

    private String imageUrl;

    private String title;

    private String body;

    private Long memberId;
}
