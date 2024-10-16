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
@Table(indexes = @Index(name = "FK_Member_Matching", columnList = "member_id"))
public class MemberMatching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_matching_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 앱 사용 회원

    private Long currentMatchedMemberId; // 현재 홈 화면에 보여지는 회원

    private Long currentMatchedMemberBookId; // 현재 홈 화면에 보여지는 회원의 책

    @Builder.Default
    private boolean isInvitationCard = Boolean.TRUE;

    @OneToMany(mappedBy = "memberMatching", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void updateCurrentMatchedInfo(Long currentMatchedMemberId, Long currentMatchedMemberBookId) {
        this.currentMatchedMemberId = currentMatchedMemberId;
        this.currentMatchedMemberBookId = currentMatchedMemberBookId;
    }

    public boolean hasCurrentMatchedInfo() {
        return currentMatchedMemberId != null && currentMatchedMemberBookId != null;
    }

    public boolean mealInvitationCard(){
        return currentMatchedMemberId == null && currentMatchedMemberBookId == null && !isInvitationCard;
    }

    public void updateInvitationCard(boolean isInvitationCard) {
        this.isInvitationCard = isInvitationCard;
    }

    public boolean getIsInvitationCard() {
        return isInvitationCard;
    }
}
