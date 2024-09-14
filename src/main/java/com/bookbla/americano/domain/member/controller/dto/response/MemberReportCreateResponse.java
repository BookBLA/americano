package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.controller.dto.response.MemberPolicyResponse.AgreedStatuses;
import com.bookbla.americano.domain.member.repository.entity.MemberReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class MemberReportCreateResponse {

    private Long memberReportId;
    private Long reporterMemberId;
    private Long reportedMemberId;
    private ReportStatuses reportStatuses;

    @Getter
    @Builder
    public static class ReportStatuses {
        private Boolean nicknameReport; // 닉네임
        private Boolean bookQuizReport; // 독서 퀴즈
        private Boolean reviewReport;   // 한 줄 감상문
        private Boolean conversationReport; // 불쾌함을 주는 대화
        private Boolean proposalReport;     // 부적절한 만남 추구
        private Boolean etcReport;      // 기타
    }

    private String reportContents;

    public static MemberReportCreateResponse from(MemberReport memberReport) {
        return MemberReportCreateResponse.builder()
            .memberReportId(memberReport.getId())
            .reporterMemberId(memberReport.getReporterMember().getId())
            .reportedMemberId(memberReport.getReportedMember().getId())
            .reportStatuses(
                ReportStatuses.builder()
                    .nicknameReport(memberReport.getNicknameReport())
                    .bookQuizReport(memberReport.getBookQuizReport())
                    .reviewReport(memberReport.getReviewReport())
                    .conversationReport(memberReport.getConversationReport())
                    .proposalReport(memberReport.getProposalReport())
                    .etcReport(memberReport.getEtcReport())
                    .build()
            )
            .reportContents(memberReport.getReportContents())
            .build();
    }

}
