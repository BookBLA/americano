package com.bookbla.americano.domain.member.repository.entity;

import java.time.LocalDate;
import java.util.Objects;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.exception.MemberProfileExceptionType;
import com.vane.badwordfiltering.BadWordFiltering;
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

    @Size(max = 1000)
    @Column(length = 1000)
    private String studentIdImageUrl;

    @Enumerated(EnumType.STRING)
    private StudentIdImageStatus studentIdImageStatus;

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

    public int calculateAge(LocalDate now) {
        LocalDate yearGap = now.minusYears(birthDate.getYear());

        return yearGap.getYear() + 1;
    }
}
