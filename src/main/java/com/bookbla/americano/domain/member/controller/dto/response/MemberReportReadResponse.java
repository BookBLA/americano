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
        private final Long reportedByMemberId;
        private ReportStatuses reportStatuses;
        private String etcContents;
    }

    @Getter
    @Builder
    public static class ReportStatuses {
        private Boolean bookQuizReport; // 독서 퀴즈
        private Boolean reviewReport; // 한 줄 감상문
        private Boolean askReport; // 개인 질문
        private Boolean replyReport; // 개인 질문 답변
        private Boolean profileImageReport; // 프로필 사진
        private Boolean etcReport; // 기타) 직접 작성
    }

    public static MemberReportReadResponse from(List<MemberReport> memberReports) {
        List<MemberReportDetail> memberReportDetails = memberReports.stream()
            .map(memberReport -> MemberReportDetail.builder()
                .memberReportId(memberReport.getId())
                .reportedByMemberId(memberReport.getReportedByMember().getId())
                .reportStatuses(ReportStatuses.builder()
                    .bookQuizReport(memberReport.getBookQuizReport())
                    .reviewReport(memberReport.getReviewReport())
                    .askReport(memberReport.getAskReport())
                    .replyReport(memberReport.getReplyReport())
                    .profileImageReport(memberReport.getProfileImageReport())
                    .etcReport(memberReport.getEtcReport())
                    .build())
                .etcContents(memberReport.getEtcContents())
                .build())
            .collect(Collectors.toList());

        return MemberReportReadResponse.builder()
            .memberReports(memberReportDetails)
            .build();
    }
}
