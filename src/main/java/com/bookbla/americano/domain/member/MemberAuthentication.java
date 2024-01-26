package com.bookbla.americano.domain.member;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.member.enums.MailAuthStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
public class MemberAuthentication extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Email schoolEmail;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private MailAuthStatus mailAuthStatus;

    private String mailAuthToken;

    private LocalDateTime mailAuthStartTime;

    private String mobileAuthToken;

    private String mobileAuthCode;

    private LocalDateTime mobileAuthStartTime;

}
