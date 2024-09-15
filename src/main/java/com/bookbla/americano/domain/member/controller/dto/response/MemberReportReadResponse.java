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
        private Boolean isNicknameReported; // 닉네임
        private Boolean isBookQuizReported; // 독서 퀴즈
        private Boolean isReviewReported;   // 한 줄 감상문
        private Boolean isConversationReported; // 불쾌함을 주는 대화
        private Boolean isProposalReported;     // 부적절한 만남 추구
        private Boolean isOtherReported;      // 기타
    }

    public static MemberReportReadResponse from(List<MemberReport> memberReports) {
        List<MemberReportDetail> memberReportDetails = memberReports.stream()
            .map(memberReport -> MemberReportDetail.builder()
                .memberReportId(memberReport.getId())
                .reportedMemberId(memberReport.getReportedMember().getId())
                .reportStatuses(ReportStatuses.builder()
                        .isNicknameReported(memberReport.getIsNicknameReported())
                        .isBookQuizReported(memberReport.getIsBookQuizReported())
                        .isReviewReported(memberReport.getIsReviewReported())
                        .isConversationReported(memberReport.getIsConversationReported())
                        .isProposalReported(memberReport.getIsProposalReported())
                        .isOtherReported(memberReport.getIsOtherReported())
                    .build())
                .reportContents(memberReport.getReportContents())
                .build())
            .collect(Collectors.toList());

        return MemberReportReadResponse.builder()
            .memberReports(memberReportDetails)
            .build();
    }
}
