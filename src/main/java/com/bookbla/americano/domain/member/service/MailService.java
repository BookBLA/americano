package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.repository.entity.Member;

public interface MailService {

    void sendEmail(Long memberId, String schoolEmail);

    void verifyEmail(Long memberId, String inputVerifyCode);

}
