package com.bookbla.americano.domain.member.repository.entity;

import java.time.LocalDateTime;

import com.bookbla.americano.domain.member.enums.EmailVerifyStatus;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAuth {

    private String schoolEmail;

    private String emailVerifyCode;

    private LocalDateTime emailVerifyStartTime;

    @Enumerated(EnumType.STRING)
    private EmailVerifyStatus emailVerifyStatus;

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

    public MemberAuth updateEmailVerifyStatus(EmailVerifyStatus emailVerifyStatus) {
        this.emailVerifyStatus = emailVerifyStatus;
        return this;
    }

    public MemberAuth updateEmailVerifyPending() {
        this.emailVerifyStatus = emailVerifyStatus.PENDING;
        return this;
    }

    public MemberAuth updateEmailVerifyDone() {
        this.emailVerifyStatus = EmailVerifyStatus.DONE;
        return this;
    }


}
