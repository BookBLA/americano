package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.exception.MemberProfileExceptionType;
import com.bookbla.americano.domain.member.exception.PolicyExceptionType;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import com.bookbla.americano.domain.school.repository.entity.School;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
public class Member extends BaseEntity {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String oauthEmail;

    private String oauthProfileImageUrl;

    @Builder.Default
    private String invitationCode = "등록되지 않음";

    @ManyToOne(fetch = FetchType.LAZY)
    @Getter(AccessLevel.NONE)
    private School school;

    private String pushToken;

    @Builder.Default
    @Column(length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean pushAlarmEnabled = Boolean.TRUE;

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

    public Member updatePushToken(String pushToken) {
        this.pushToken = pushToken;
        return this;
    }

    public Member updatePushTokenEnabled(Boolean pushAlarmEnabled) {
        this.pushAlarmEnabled = pushAlarmEnabled;
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
        this.memberProfile
                .updateName(memberProfile.getName())
                .updateBirthDate(memberProfile.getBirthDate())
                .updateGender(memberProfile.getGender())
                .updateSchoolEmail(memberProfile.getSchoolEmail())
                .updatePhoneNumber(memberProfile.getPhoneNumber())
                .updateProfileImageUrl(memberProfile.getProfileImageUrl())
                .updateOpenKakaoRoomUrl(memberProfile.getOpenKakaoRoomUrl())
                .updateStudentIdImageUrl(memberProfile.getStudentIdImageUrl());

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
            throw new BaseException(MemberExceptionType.STYLE_ALREADY_REGISTERED);
        }
    }

    public School getSchool() {
        if (Objects.isNull(school)) {
            return School.notRegistered();
        }
        return school;
    }

    public void revertStatus(MemberStatusLogRepository memberStatusLogRepository) {
        MemberStatusLog memberStatusLog = memberStatusLogRepository.getByMemberIdOrThrow(this.getId());
        this.updateMemberStatus(memberStatusLog.getBeforeStatus(), LocalDateTime.now());
        memberStatusLogRepository.delete(memberStatusLog);
    }

    public Member updateInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
        return this;
    }

    public void updateSchool(School school) {
        this.school = school;
    }

    public boolean isWoman() {
        return memberProfile.getGender() == Gender.FEMALE;
    }

    public boolean canChangeToComplete(MemberStatus afterStatus) {
        return memberStatus == MemberStatus.BOOK && afterStatus == MemberStatus.COMPLETED;
    }
}
