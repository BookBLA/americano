package com.bookbla.americano.domain.member.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MailSendResponse {

    private final String message;
}
