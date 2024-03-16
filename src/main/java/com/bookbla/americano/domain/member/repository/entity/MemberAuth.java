package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.member.enums.MailVerifyStatus;
import java.time.LocalDateTime;
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
public class MemberAuth extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(unique = true)
    private String schoolEmail;

    private String emailVerifyCode;

    private LocalDateTime emailVerifyStartTime;

    private String phoneNumber;

    private String studentIdImageUrl;

    @Enumerated(EnumType.STRING)
    private MailVerifyStatus mailVerifyStatus;

    public MemberAuth updateSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
        return this;
    }

    public MemberAuth updateEmailVerifyCode(String emailVerifyCode) {
        this.emailVerifyCode = emailVerifyCode;
        return this;
    }

    public MemberAuth updateEmailVerifyStartTime(LocalDateTime emailVerifyStartTime) {
        this.emailVerifyStartTime = emailVerifyStartTime;
        return this;
    }

    public MemberAuth updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public MemberAuth updateStudentIdImageUrl(String studentIdImageUrl) {
        this.studentIdImageUrl = studentIdImageUrl;
        return this;
    }

    public MemberAuth updateMailVerifyDone() {
        this.mailVerifyStatus = MailVerifyStatus.DONE;
        return this;
    }

}
