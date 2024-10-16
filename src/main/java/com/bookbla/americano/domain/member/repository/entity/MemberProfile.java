package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.base.utils.BadWordFilterUtil;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.StudentIdImageStatus;
import com.bookbla.americano.domain.member.exception.MemberProfileExceptionType;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;
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
    @Builder.Default
    private StudentIdImageStatus studentIdImageStatus = StudentIdImageStatus.UNREGISTER;

    public MemberProfile updateName(String name) {
        if (!verifyNickname(name)) {
            throw new BaseException(MemberProfileExceptionType.CONTAIN_BAD_WORDS);
        }

        this.name = name;

        return this;
    }

    public static Boolean verifyNickname(String name) {
        return BadWordFilterUtil.verify(name) && !name.contains("북블라");
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
