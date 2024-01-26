package com.bookbla.americano.domain.member;

import com.bookbla.americano.base.entity.BaseInsertEntity;
import com.bookbla.americano.domain.member.enums.MailAuthStatus;
import com.bookbla.americano.domain.member.enums.MemberType;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MemberAuthentication extends BaseInsertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long user_id;

    private Email email;

    private String password;

    private String salt; // 보류

    private MemberType memberType;

    private MailAuthStatus mailAuthStatus;

    private String mailAuthToken;

    private LocalDateTime mailAuthStartTime;

    private String mobileAuthToken;

    private LocalDateTime mobileAuthStartTime;

    private String mobileAuthCode;
}
