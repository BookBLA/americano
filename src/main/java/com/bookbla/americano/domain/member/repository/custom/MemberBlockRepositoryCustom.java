package com.bookbla.americano.domain.member.repository.custom;

import java.util.List;
import java.util.Set;

public interface MemberBlockRepositoryCustom {

    // 앱 사용자가 차단한 회원
    List<Long> getBlockedMemberIdsByBlockerMemberId(Long MemberId, Set<Long> matchingMembers);
}
