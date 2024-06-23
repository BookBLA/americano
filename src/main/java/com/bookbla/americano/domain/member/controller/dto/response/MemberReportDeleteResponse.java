package com.bookbla.americano.domain.member.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberReportDeleteResponse {

    private Long memberReportId;

    public static MemberReportDeleteResponse from(Long memberReportId) {
        return MemberReportDeleteResponse.builder()
            .memberReportId(memberReportId)
            .build();
    }
}
