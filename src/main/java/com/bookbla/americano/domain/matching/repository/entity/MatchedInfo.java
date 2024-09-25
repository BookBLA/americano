package com.bookbla.americano.domain.matching.repository.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MatchedInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long matchedMemberId;

    private Long matchedMemberBookId;

    @Builder.Default
    private Double similarityWeight = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_matching_id")
    private MemberMatching memberMatching;

    public void accumulateSimilarityWeight(Double similarityWeight) {
        this.similarityWeight += similarityWeight;
    }

    public static MatchedInfo from(Long memberId, Long matchedMemberId, Long matchedMemberBookId) {
        return MatchedInfo.builder()
                .memberId(memberId)
                .matchedMemberId(matchedMemberId)
                .matchedMemberBookId(matchedMemberBookId)
                .build();
    }
}
