package com.bookbla.americano.domain.member.repository.custom;

import java.util.List;
import java.util.Set;

public interface MemberVerifyRepositoryCustom {

    //최초 가입 후 이틀이 지났을때, 학생증 인증된 상태만 추천 ,지나지 않았다면, 학생증 상태 상관없이 추천
    List<Long> getMemberIdsByStudentIdVerify(Set<Long> MemberId);
}