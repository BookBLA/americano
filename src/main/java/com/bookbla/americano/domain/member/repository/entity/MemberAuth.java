package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.member.Email;
import com.bookbla.americano.domain.member.enums.EmailVerifyStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
