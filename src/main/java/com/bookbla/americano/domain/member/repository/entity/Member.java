package com.bookbla.americano.domain.member.repository.entity;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import javax.persistence.Embedded;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Embedded
    private MemberAuth memberAuth;

    @Embedded
    private MemberProfile memberProfile;

    @Embedded
    private MemberStyle memberStyle;

    public Member updateOauthEmail(String oauthEmail) {
        this.oauthEmail = oauthEmail;
        return this;
    }

    public Member updateMemberType(MemberType memberType) {
        this.memberType = memberType;
        return this;
    }

    public Member updateMemberStatus(MemberStatus memberStatus) {
        this.memberStatus = memberStatus;
        return this;
    }

}
