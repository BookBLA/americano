package com.bookbla.americano.domain.member.service;

import com.bookbla.americano.domain.member.repository.entity.Member;

public interface MemberService {

    Member getMemberById(Long memberId);

}
