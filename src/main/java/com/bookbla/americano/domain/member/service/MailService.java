package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.repository.entity.Member;

public interface MailService {

    void sendEmail(Member member, String schoolEmail);

    void verifyEmail(Member member, String inputVerifyCode);

}
