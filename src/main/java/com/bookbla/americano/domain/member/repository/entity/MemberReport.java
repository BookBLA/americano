package com.bookbla.americano.domain.member.repository.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_member_id")
    private Member reporterMember; // 신고를 한 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_member_id")
    private Member reportedMember; // 신고를 당한 유저

    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isNicknameReported; // 닉네임

    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isBookQuizReported; // 독서 퀴즈

    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isReviewReported; // 한 줄 감상문

    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isConversationReported; // 불쾌함을 주는 대화

    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isProposalReported; // 부적절한 만남 추구

    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean isOtherReported;     // 기타

    private String reportContents; // 신고 사유

    public boolean hasAllReportsFalse() {
        return Boolean.FALSE.equals(isNicknameReported) &&
            Boolean.FALSE.equals(isBookQuizReported) &&
            Boolean.FALSE.equals(isReviewReported) &&
            Boolean.FALSE.equals(isConversationReported) &&
            Boolean.FALSE.equals(isProposalReported) &&
            Boolean.FALSE.equals(reportContents);
    }

}
