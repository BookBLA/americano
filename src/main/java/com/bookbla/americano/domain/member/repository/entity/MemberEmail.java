package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.EmailVerifyStatus;
import com.bookbla.americano.domain.member.exception.MemberEmailExceptionType;

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
@Table(indexes = @Index(name = "FK_Member_Email", columnList = "member_id"))
public class MemberEmail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String schoolEmail;

    private String verifyCode;

    @Enumerated(EnumType.STRING)
    private EmailVerifyStatus emailVerifyStatus;

    public void validatePending() {
        if (emailVerifyStatus == EmailVerifyStatus.PENDING) {
            throw new BaseException(MemberEmailExceptionType.STILL_PENDING);
        }
    }

    public MemberEmail updateSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
        return this;
    }

    public MemberEmail updateVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
        return this;
    }

    public MemberEmail updateEmailVerifyPending() {
        this.emailVerifyStatus = EmailVerifyStatus.PENDING;
        return this;
    }

    public MemberEmail updateEmailVerifyDone() {
        this.emailVerifyStatus = EmailVerifyStatus.DONE;
        return this;
    }

    public String getEmailDomain() {
        int dotIndex = schoolEmail.indexOf("@");
        return schoolEmail.substring(dotIndex, schoolEmail.length());
    }
}
