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
        private Boolean bookQuizReport; // 독서 퀴즈
        private Boolean reviewReport; // 한 줄 감상문
        private Boolean askReport; // 개인 질문
        private Boolean replyReport; // 개인 질문 답변
        private Boolean profileImageReport; // 프로필 사진
        private Boolean etcReport; // 기타) 직접 작성
    }

    private String etcContents;

    public static MemberReportCreateResponse from(MemberReport memberReport) {
        return MemberReportCreateResponse.builder()
            .memberReportId(memberReport.getId())
            .reporterMemberId(memberReport.getReporterMember().getId())
            .reportedMemberId(memberReport.getReportedMember().getId())
            .reportStatuses(
                ReportStatuses.builder()
                    .bookQuizReport(memberReport.getBookQuizReport())
                    .reviewReport(memberReport.getReviewReport())
                    .askReport(memberReport.getAskReport())
                    .replyReport(memberReport.getReplyReport())
                    .profileImageReport(memberReport.getProfileImageReport())
                    .etcReport(memberReport.getEtcReport())
                    .build()
            )
            .etcContents(memberReport.getEtcContents())
            .build();
    }

}
