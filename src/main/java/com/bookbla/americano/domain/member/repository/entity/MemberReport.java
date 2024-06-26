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
    private Boolean bookQuizReport; // 독서 퀴즈
    
    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean reviewReport; // 한 줄 감상문 

    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean askReport; // 개인 질문

    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean replyReport; // 개인 질문 답변

    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean profileImageReport; // 프로필 사진

    // 기타 내용
    private String etcContents;

    public boolean hasAllReportsFalse() {
        return Boolean.FALSE.equals(bookQuizReport) &&
            Boolean.FALSE.equals(reviewReport) &&
            Boolean.FALSE.equals(askReport) &&
            Boolean.FALSE.equals(profileImageReport) &&
            Boolean.FALSE.equals(replyReport);
    }

}
