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
    private Long reportedByMemberId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class reportStatuses {
        private Boolean bookQuizReport; // 독서 퀴즈
        private Boolean reviewReport; // 한 줄 감상문
        private Boolean askReport; // 개인 질문
        private Boolean replyReport; // 개인 질문 답변
        private Boolean profileImageReport; // 프로필 사진
        private Boolean etcReport; // 기타) 직접 작성
    }

    @NotNull(message = "신고 항목이 입력되지 않았습니다.")
    private reportStatuses reportStatuses;

    @NotNull(message = "기타 항목이 입력되지 않았습니다. (없을 시에 빈 칸으로 입력)")
    private String etcContents;
}
