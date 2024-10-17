package com.bookbla.americano.domain.matching.repository.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(indexes = @Index(name = "FK_MemberMatching_MatchIgnored", columnList = "member_matching_id"))
public class MatchIgnoredInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long ignoredMemberId;

    private Long ignoredMemberBookId;

    private LocalDateTime ignoredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_matching_id")
    private MemberMatching memberMatching;

    public static MatchIgnoredInfo from(Long memberId, Long ignoredMemberId, Long ignoredMemberBookId) {
        return MatchIgnoredInfo.builder()
                .memberId(memberId)
                .ignoredMemberId(ignoredMemberId)
                .ignoredMemberBookId(ignoredMemberBookId)
                .ignoredAt(LocalDateTime.now())
                .build();
    }

    public void updateIgnoredAt() {
        this.ignoredAt = LocalDateTime.now();
    }
}
