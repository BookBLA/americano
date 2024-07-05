package com.bookbla.americano.domain.school.repository.entity;

import java.util.ArrayList;
import java.util.List;

import com.bookbla.americano.base.entity.BaseUpdateEntity;
import com.bookbla.americano.domain.member.repository.entity.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Getter
public class School extends BaseUpdateEntity {

    public static final int OPEN_STANDARD = 30;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String emailDomain;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "school")
    private List<Member> members = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private SchoolStatus schoolStatus = SchoolStatus.CLOSED;

    public static School notRegistered() {
        return School.builder().name("등록되지 않음").build();
    }

    public int validMemberCounts() {
        return (int) members.stream()
                .filter(Member::isWoman)
                .filter(it -> it.getMemberStatus().isApproved())
                .count();
    }

    public void checkOpen() {
        if (schoolStatus == SchoolStatus.OPEN) {
            return;
        }
        if (validMemberCounts() >= OPEN_STANDARD) {
            schoolStatus = SchoolStatus.OPEN;
        }
    }

    public int getOpenStandard() {
        return OPEN_STANDARD;
    }
}
