package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.controller.dto.response.MemberAuthUpdateResponse;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.service.dto.MemberAuthDto;

public interface MemberAuthService {

    void createMemberAuth(MemberAuthDto memberAuthDto);

    MemberAuthUpdateResponse updateMemberAuth(Member member,
        String phoneNumber, String studentIdImageUrl);
}
