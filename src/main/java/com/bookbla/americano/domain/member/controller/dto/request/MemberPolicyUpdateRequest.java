package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.controller.dto.request.MailSendRequest.AgreedStatuses;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberPolicyUpdateRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateStatuses {
        private boolean adAgreementPolicy;
    }

    @NotNull(message = "약관의 동의여부가 입력되지 않았습니다.")
    private UpdateStatuses updateStatuses;

}
