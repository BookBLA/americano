package com.bookbla.americano.domain.matching.repository.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(indexes = @Index(name = "FKlr3wv90b1rtb6v0s4mjdhc0vl", columnList = "member_matching_id"))
public class MatchExcludedInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long excludedMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_matching_id")
    private MemberMatching memberMatching;

    public static MatchExcludedInfo of(Long memberId, Long excludedMemberId) {
        return MatchExcludedInfo.builder()
                .memberId(memberId)
                .excludedMemberId(excludedMemberId)
                .build();
    }
}
