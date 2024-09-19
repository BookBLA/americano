package com.bookbla.americano.domain.matching.repository.entity;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberMatching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_matching_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 앱 사용 회원

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "member_match_matched", joinColumns = @JoinColumn(name = "member_matching_id"))
    private Map<Long, Long> matched = new HashMap<>(); // 매칭된 회원, 매칭된 회원의 책

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "member_match_excluded", joinColumns = @JoinColumn(name = "member_matching_id"))
    private Set<Long> excluded = new HashSet<>(); // 매칭에서 제외된 회원

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "member_match_ignored_member_book", joinColumns = @JoinColumn(name = "member_matching_id"))
    private Map<Long, Long> ignoredMemberAndBook = new HashMap<>();

    public void addMatchedMember(Long memberId, Long memberBookId) {
        matched.put(memberId, memberBookId);
    }

    public void addExcludedMember(Long memberId) {
        excluded.add(memberId);
    }
}
