package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.auth.exception.LoginExceptionType;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.exception.MemberProfileExceptionType;
import com.bookbla.americano.domain.member.exception.PolicyExceptionType;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseInsertEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oauthEmail;

    private String oauthProfileImageUrl;

    private String pushToken;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus = MemberStatus.PROFILE;

    private LocalDateTime deleteAt;

    private LocalDateTime statusModifiedAt;

    @Embedded
    @Getter(AccessLevel.NONE)
    private MemberProfile memberProfile;

    @Embedded
    @Getter(AccessLevel.NONE)
    private MemberPolicy memberPolicy;

    @Embedded
    @Getter(AccessLevel.NONE)
    private MemberStyle memberStyle;

    @OneToMany(mappedBy = "blockerMember")
    private Set<MemberBlock> blockerMembers = new HashSet<>();

    @OneToMany(mappedBy = "blockedMember")
    private Set<MemberBlock> blockedMembers = new HashSet<>();

    @OneToMany(mappedBy = "reporterMember")
    private Set<MemberReport> reporterMembers = new HashSet<>();

    @OneToMany(mappedBy = "reportedMember")
    private Set<MemberReport> reportedMembers = new HashSet<>();

    @Builder.Default
    private Integer reportedCount = 0; // 신고당한 횟수

    // 첫 가입시 확인용
    public void updateMemberStatus() {
        if (memberProfile.isCertified() && memberStatus == MemberStatus.APPROVAL) {
            this.memberStatus = MemberStatus.STYLE;
        }
    }

    public Member updateOauthEmail(String oauthEmail) {
        this.oauthEmail = oauthEmail;
        return this;
    }

    public Member updatePushToken(String pushToken) {
        this.pushToken = pushToken;
        return this;
    }

    public Member updateMemberType(MemberType memberType) {
        this.memberType = memberType;
        return this;
    }

    public Member updateMemberStatus(MemberStatus memberStatus, LocalDateTime statusModifiedAt) {
        this.memberStatus = memberStatus;
        this.statusModifiedAt = statusModifiedAt;
        return this;
    }

    public Member updateDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
        return this;
    }

    public Member updateMemberProfile(MemberProfile memberProfile) {
        this.memberProfile = memberProfile;
        return this;
    }

    public Member updateMemberPolicy(MemberPolicy memberPolicy) {
        this.memberPolicy = memberPolicy;
        return this;
    }

    public Member updateMemberStyle(MemberStyle memberStyle) {
        this.memberStyle = memberStyle;
        return this;
    }

    public Member updateReportedCountUp() {
        this.reportedCount += 1;
        return this;
    }

    public Member updateReportedCountDown() {
        if (this.reportedCount <= 0) {
            this.reportedCount = 0;
            return this;
        }
        this.reportedCount -= 1;
        return this;
    }

    public boolean canSendAdvertisementAlarm() {
        return pushToken != null && memberPolicy.getAdAgreementPolicy();
    }

    public boolean hasProfile() {
        return memberProfile != null;
    }

    public MemberProfile getMemberProfile() {
        if (memberProfile == null) {
            throw new BaseException(MemberProfileExceptionType.PROFILE_NOT_FOUND);
        }
        return memberProfile;
    }

    public MemberStyle getMemberStyle() {
        if (memberStyle == null) {
            throw new BaseException(MemberExceptionType.STYLE_NOT_REGISTERED);
        }
        return memberStyle;
    }

    public MemberPolicy getMemberPolicy() {
        if (memberPolicy == null) {
            throw new BaseException(PolicyExceptionType.MEMBER_NOT_REGISTERED);
        }
        return memberPolicy;
    }

    public void validateStyleRegistered() {
        if (this.memberStyle != null) {
            throw new BaseException(MemberExceptionType.STYLE_ALREADY_REGISTERD);
        }
    }

    public void revertStatus(MemberStatusLogRepository memberStatusLogRepository) {
        MemberStatusLog memberStatusLog = memberStatusLogRepository.getByMemberIdOrThrow(this.getId());
        this.updateMemberStatus(memberStatusLog.getBeforeStatus(), LocalDateTime.now());
        memberStatusLogRepository.delete(memberStatusLog);
    }
}
