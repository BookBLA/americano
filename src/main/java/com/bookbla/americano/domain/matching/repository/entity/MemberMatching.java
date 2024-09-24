package com.bookbla.americano.domain.matching.repository.entity;

import com.bookbla.americano.domain.member.repository.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "memberMatching")
    private List<MatchedInfo> matched; // 필터링을 거친 매칭된 회원 저장

    @OneToMany(mappedBy = "memberMatching")
    private List<MatchExcludedInfo> excluded; // 매칭에서 제외된 회원

    @OneToMany(mappedBy = "memberMatching")
    private List<MatchIgnoredInfo> ignoredMemberAndBook; // 매칭에서 제외된 회원 + 책

    public static MemberMatching of(Member member) {
        return MemberMatching.builder()
                .member(member)
                .matched(new ArrayList<>())
                .excluded(new ArrayList<>())
                .ignoredMemberAndBook(new ArrayList<>())
                .build();
    }
}
