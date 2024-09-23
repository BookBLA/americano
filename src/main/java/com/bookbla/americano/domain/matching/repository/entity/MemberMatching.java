package com.bookbla.americano.domain.matching.repository.entity;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    @OneToMany(mappedBy = "memberMatching")
    private List<MatchedInfo> matched = new ArrayList<>(); // 필터링을 거친 매칭된 회원 저장

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "member_match_excluded", joinColumns = @JoinColumn(name = "member_matching_id"))
    private Set<Long> excluded = new HashSet<>(); // 매칭에서 제외된 회원

    @Builder.Default
    @OneToMany(mappedBy = "memberMatching")
    private List<MatchedInfo> ignoredMemberAndBook = new ArrayList<>(); // 매칭에서 제외된 회원

    // TODO: matched 저장 쿼리 나가는지 확인
    public void saveAndUpdateMatched(List<MatchedInfo> matchingMembers) {
        matchingMembers.forEach(matchedInfo -> matchedInfo.updateMemberMatching(this));
        this.matched = matchingMembers;
    }

    public void sortMatched() {
        // 가중치 큰 순으로 정렬
        matched.sort((matched1, matched2) ->
                Double.compare(matched2.getSimilarityWeight(), matched1.getSimilarityWeight()));
    }

    public void addExcludedMember(Long memberId) {
        excluded.add(memberId);
    }

    public void addIgnoredMemberAndBook(Long memberId, Long memberBookId) {
        ignoredMemberAndBook.add(MatchedInfo.from(memberId, memberBookId));
    }

    public static MemberMatching of(Member member) {
        return MemberMatching.builder()
                .member(member)
                .build();
    }
}
