package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.member.enums.EmailVerifyStatus;
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
public class MemberEmail extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String schoolEmail;

    @Enumerated(EnumType.STRING)
    private EmailVerifyStatus emailVerifyStatus;

    public MemberEmail updateSchoolEmail(String schoolEmail) {
        this.schoolEmail = schoolEmail;
        return this;
    }

    public MemberEmail updateEmailVerifyStatus(EmailVerifyStatus emailVerifyStatus) {
        this.emailVerifyStatus = emailVerifyStatus;
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
}
