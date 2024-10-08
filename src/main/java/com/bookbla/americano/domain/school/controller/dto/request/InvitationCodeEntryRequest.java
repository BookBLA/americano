package com.bookbla.americano.domain.school.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class InvitationCodeEntryRequest {

    @NotNull(message = "초대코드가 입력되지 않았습니다.")
    private String invitationCode;

}
