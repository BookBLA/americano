package com.bookbla.americano.domain.matching.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MatchIgnoredInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long ignoredMemberId;

    private Long ignoredMemberBookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_matching_id")
    private MemberMatching memberMatching;

    public static MatchIgnoredInfo from(Long memberId, Long ignoredMemberId, Long ignoredMemberBookId) {
        return MatchIgnoredInfo.builder()
                .memberId(memberId)
                .ignoredMemberId(ignoredMemberId)
                .ignoredMemberBookId(ignoredMemberBookId)
                .build();
    }
}
