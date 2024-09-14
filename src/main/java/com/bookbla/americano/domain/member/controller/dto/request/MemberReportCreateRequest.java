package com.bookbla.americano.domain.member.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberReportCreateRequest {

    @NotNull(message = "신고당하는 멤버의 아이디가 입력되지 않았습니다.")
    private Long reportedMemberId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class reportStatuses {
        private Boolean nicknameReport; // 닉네임
        private Boolean bookQuizReport; // 독서 퀴즈
        private Boolean reviewReport;   // 한 줄 감상문
        private Boolean conversationReport; // 불쾌함을 주는 대화
        private Boolean proposalReport;     // 부적절한 만남 추구
        private Boolean etcReport;      // 기타
    }

    @NotNull(message = "신고 항목이 입력되지 않았습니다.")
    private reportStatuses reportStatuses;

    private String reportContents;
}
