package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.domain.member.enums.Gender;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
public class MemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;

    private String major;

    @Column(nullable = false)
    private LocalDate birthDate;

    private String profileImageUrl;

    private String studentNumber;

    private String schoolName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true)
    private String nickname;

    private String openKakaoRoomUrl;

    public MemberProfile updateName(String name) {
        this.name = name;
        return this;
    }

    public MemberProfile updateBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public MemberProfile updateSchoolName(String schoolName) {
        this.schoolName = schoolName;
        return this;
    }

    public MemberProfile updateGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public MemberProfile updateOpenKakaoRoomUrl(String openKakaoRoomUrl) {
        this.openKakaoRoomUrl = openKakaoRoomUrl;
        return this;
    }

}