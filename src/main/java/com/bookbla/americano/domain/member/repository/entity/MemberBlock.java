package com.bookbla.americano.domain.member.repository.entity;

import javax.persistence.*;

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
@Table(indexes = {
        @Index(name = "FK4xkll2ajl8tth6adcxpsc0v22", columnList = "blocker_member_id"),
        @Index(name = "FKd1lffv8jq5qpswx890s4vww7k", columnList = "blocked_member_id")
})
public class MemberBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_member_id")
    private Member blockerMember; // 차단을 한 유저

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_member_id")
    private Member blockedMember; // 차단된 유저

}
