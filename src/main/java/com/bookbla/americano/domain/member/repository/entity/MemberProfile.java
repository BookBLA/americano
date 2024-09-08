package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.OpenKakaoRoomStatus;
import com.bookbla.americano.domain.member.enums.ProfileImageStatus;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.exception.MemberProfileExceptionType;
import com.vane.badwordfiltering.BadWordFiltering;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfile {

    @Column(unique = true)
    private String name;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String schoolEmail;

    private String phoneNumber;

    @Size(max = 1000)
    @Column(length = 1000)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private ProfileImageStatus profileImageStatus;

    @Size(max = 1000)
    @Column(length = 1000)
    private String openKakaoRoomUrl;

    @Enumerated(EnumType.STRING)
    private OpenKakaoRoomStatus openKakaoRoomStatus;

    @Size(max = 1000)
    @Column(length = 1000)
    private String studentIdImageUrl;

    @Enumerated(EnumType.STRING)
    private StudentIdImageStatus studentIdImageStatus;

    private String major;

    private String studentNumber;

    private String nickname;

    public String showBlindName() {
        String familyName = name.split("")[0];
        return familyName + "OO";
    }

    public int calculateAge(LocalDate now) {
        int age = Period.between(birthDate, now).getYears();

        if (now.isBefore(birthDate.plusYears(age))) {
            age--;
        }

        return age;
    }

    public boolean isCertified() {
        return profileImageStatus == ProfileImageStatus.DONE
                && openKakaoRoomStatus == OpenKakaoRoomStatus.DONE
                && studentIdImageStatus == StudentIdImageStatus.DONE;
    }

    public MemberProfile updateName(String name) {
        BadWordFiltering badWordFiltering = new BadWordFiltering();

        if (badWordFiltering.blankCheck(name)) {
            throw new BaseException(MemberProfileExceptionType.CONTAIN_BAD_WORDS);
        }

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

    public MemberProfile updateSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
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

    public String getGenderName() {
        if (Objects.isNull(gender)) {
            return "등록되지 않음";
        }
        return getGender().name();
    }

}
