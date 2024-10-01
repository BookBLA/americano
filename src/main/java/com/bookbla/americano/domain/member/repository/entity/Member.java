package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseEntity;
import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.matching.exception.MemberMatchingExceptionType;
import com.bookbla.americano.domain.member.enums.Gender;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import com.bookbla.americano.domain.member.exception.MemberBookmarkExceptionType;
import com.bookbla.americano.domain.member.exception.MemberExceptionType;
import com.bookbla.americano.domain.member.exception.MemberProfileExceptionType;
import com.bookbla.americano.domain.member.exception.PolicyExceptionType;
import com.bookbla.americano.domain.member.repository.MemberStatusLogRepository;
import com.bookbla.americano.domain.school.repository.entity.School;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.bookbla.americano.domain.member.enums.StudentIdImageStatus.DONE;

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

    @Builder.Default
    private String invitationCode = "등록되지 않음";

    @ManyToOne(fetch = FetchType.EAGER)
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

    @LastModifiedDate
    private LocalDateTime lastUsedAt;

    @Embedded
    @Getter(AccessLevel.NONE)
    private MemberModal memberModal;

    @Embedded
    @Getter(AccessLevel.NONE)
    private MemberProfile memberProfile;

    @Embedded
    @Getter(AccessLevel.NONE)
    private MemberPolicy memberPolicy;

    @Embedded
    @Getter(AccessLevel.NONE)
    private MemberStyle memberStyle;

    @Builder.Default
    @OneToMany(mappedBy = "blockerMember")
    private Set<MemberBlock> blockerMembers = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "blockedMember")
    private Set<MemberBlock> blockedMembers = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "reporterMember")
    private Set<MemberReport> reporterMembers = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "reportedMember")
    private Set<MemberReport> reportedMembers = new HashSet<>();

    @Builder.Default
    private Integer reportedCount = 0; // 신고당한 횟수

    @Column
    @Builder.Default
    private Integer newPersonAdmobCount = 3;

    @Column
    @Builder.Default
    private Integer freeBookmarkAdmobCount = 2;

    @Column
    @Builder.Default
    private Integer initialRewardBookmarkCount = 4;

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

    public Member updateLastUsedAt() {
        this.lastUsedAt = LocalDateTime.now();
        return this;
    }

    public Member updateMemberProfile(MemberProfile memberProfile) {
        this.memberProfile = Optional.ofNullable(this.memberProfile)
            .orElse(new MemberProfile());

        this.memberProfile
                .updateName(memberProfile.getName())
                .updateBirthDate(memberProfile.getBirthDate())
                .updateGender(memberProfile.getGender())
                .updateSchoolEmail(memberProfile.getSchoolEmail());
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

    public MemberModal getMemberModal() {
        if (memberModal == null) {
            memberModal = MemberModal.builder().build();
        }
        return memberModal;
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

    public void validateStudentIdStatusRegistered() {
        if (this.memberProfile.getStudentIdImageStatus() != DONE) {
            throw new BaseException(MemberProfileExceptionType.STUDENT_ID_NOT_VALID);
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

    public boolean isMan() {
        return !isWoman();
    }

    public boolean canChangeToApproval(MemberStatus afterStatus) {
        return memberStatus == MemberStatus.BOOK && afterStatus == MemberStatus.APPROVAL;
    }

    public void watchBookmarkAdmob() {
        if (freeBookmarkAdmobCount <= 0) {
            throw new BaseException(MemberBookmarkExceptionType.ADMOB_COUNT_NOT_VALID);
        }
        this.freeBookmarkAdmobCount--;
    }

    public void watchNewPersonAdmob() {
        if (newPersonAdmobCount <= 0) {
            throw new BaseException(MemberMatchingExceptionType.EXCEED_MAX_RECOMMENDATION);
        }
        this.newPersonAdmobCount--;
    }

    public boolean canGiveInitialBookmarkReward() {
        return this.initialRewardBookmarkCount > 0;
    }

    public void useInitialAddBookBookmarkCount() {
        if (initialRewardBookmarkCount <= 0) {
            throw new BaseException(MemberBookmarkExceptionType.INVALID_BOOKMARK_REWARD_COUNT);
        }
        this.initialRewardBookmarkCount--;
    }
}
