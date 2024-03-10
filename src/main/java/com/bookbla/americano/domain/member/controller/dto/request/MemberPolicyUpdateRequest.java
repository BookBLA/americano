package com.bookbla.americano.domain.member.controller.dto.request;

import java.util.ArrayList;
import java.util.List;
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
        private Boolean adAgreementPolicy;

        public List<Boolean> toList() {
            List<Boolean> updateStatuses = new ArrayList<>();
            updateStatuses.add(adAgreementPolicy);
            return updateStatuses;
        }
    }

    @NotNull(message = "선택 약관이 입력되지 않았습니다.")
    private UpdateStatuses updateStatuses;

}
