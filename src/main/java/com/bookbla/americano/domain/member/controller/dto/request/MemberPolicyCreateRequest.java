package com.bookbla.americano.domain.member.controller.dto.request;

import com.bookbla.americano.domain.member.service.dto.MemberPolicyDto;
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
public class MemberPolicyCreateRequest {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgreedStatuses {
        private Boolean adAgreementPolicy;

        public List<Boolean> toList() {
            List<Boolean> agreedStatuses = new ArrayList<>();
            agreedStatuses.add(adAgreementPolicy);
            return agreedStatuses;
        }
    }

    @NotNull(message = "선택 약관이 입력되지 않았습니다.")
    private AgreedStatuses agreedStatuses;

    public MemberPolicyDto toDto() {
        return MemberPolicyDto.builder()
            .agreedStatuses(agreedStatuses.toList())
            .build();
    }

}
