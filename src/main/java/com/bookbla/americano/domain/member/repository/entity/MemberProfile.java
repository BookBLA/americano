package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import java.time.LocalDate;
import java.time.Period;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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

    @Column(nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String schoolName;

    private String phoneNumber;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private ProfileImageStatus profileImageStatus;

    private String openKakaoRoomUrl;

    @Enumerated(EnumType.STRING)
    private OpenKakaoRoomStatus openKakaoRoomStatus;

    private String studentIdImageUrl;

    @Enumerated(EnumType.STRING)
    private StudentIdImageStatus studentIdImageStatus;

    private String major;

    private String studentNumber;

    private String nickname;


    public String showBlindName() {
        String firstName = name.split("")[0];
        return firstName + "OO";
    }

    public int calculateAge(LocalDate now) {
        int age = Period.between(birthDate, now).getYears();

        if (now.isBefore(birthDate.plusYears(age))) {
            age--;
        }

        return age;
    }

    public MemberProfile updateName(String name) {
        this.name = name;
        return this;
    }

    public MemberProfile updateBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public MemberProfile updateGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public MemberProfile updateSchoolName(String schoolName) {
        this.schoolName = schoolName;
        return this;
    }

    public MemberProfile updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public MemberProfile updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        return this;
    }

    public MemberProfile updateProfileImageStatus(ProfileImageStatus profileImageStatus) {
        this.profileImageStatus = profileImageStatus;
        return this;
    }

    public MemberProfile updateOpenKakaoRoomUrl(String openKakaoRoomUrl) {
        this.openKakaoRoomUrl = openKakaoRoomUrl;
        return this;
    }

    public MemberProfile updateOpenKakaoRoomStatus(OpenKakaoRoomStatus openKakaoRoomStatus) {
        this.openKakaoRoomStatus = openKakaoRoomStatus;
        return this;
    }

    public MemberProfile updateStudentIdImageUrl(String studentIdImageUrl) {
        this.studentIdImageUrl = studentIdImageUrl;
        return this;
    }

    public MemberProfile updateStudentIdImageStatus(StudentIdImageStatus studentIdImageStatus) {
        this.studentIdImageStatus = studentIdImageStatus;
        return this;
    }

}
