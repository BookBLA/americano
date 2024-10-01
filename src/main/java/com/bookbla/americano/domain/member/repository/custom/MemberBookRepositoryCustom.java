package com.bookbla.americano.domain.member.repository.custom;

import com.bookbla.americano.domain.member.repository.entity.MemberBook;

import java.util.Collection;
import java.util.List;

public interface MemberBookRepositoryCustom {

    List<MemberBook> findAllByMemberBookId(Collection<Long> memberBookId);
}
