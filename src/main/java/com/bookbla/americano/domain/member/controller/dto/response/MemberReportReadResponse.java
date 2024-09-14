package com.bookbla.americano.domain.member.controller.dto.response;

import com.bookbla.americano.domain.member.repository.entity.MemberReport;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberReportReadResponse {

    private final List<MemberReportDetail> memberReports;

    @Getter
    @Builder
    public static class MemberReportDetail {
        private final Long memberReportId;
        private final Long reportedMemberId;
        private ReportStatuses reportStatuses;
        private String reportContents;
    }

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

    public static MemberReportReadResponse from(List<MemberReport> memberReports) {
        List<MemberReportDetail> memberReportDetails = memberReports.stream()
            .map(memberReport -> MemberReportDetail.builder()
                .memberReportId(memberReport.getId())
                .reportedMemberId(memberReport.getReportedMember().getId())
                .reportStatuses(ReportStatuses.builder()
                        .nicknameReport(memberReport.getNicknameReport())
                        .bookQuizReport(memberReport.getBookQuizReport())
                        .reviewReport(memberReport.getReviewReport())
                        .conversationReport(memberReport.getConversationReport())
                        .proposalReport(memberReport.getProposalReport())
                        .etcReport(memberReport.getEtcReport())
                    .build())
                .reportContents(memberReport.getReportContents())
                .build())
            .collect(Collectors.toList());

        return MemberReportReadResponse.builder()
            .memberReports(memberReportDetails)
            .build();
    }
}
