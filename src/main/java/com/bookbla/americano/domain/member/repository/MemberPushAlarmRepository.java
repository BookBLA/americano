package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import com.bookbla.americano.domain.member.repository.entity.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPushAlarmRepository extends JpaRepository<MemberPushAlarm, Long> {

    List<MemberPushAlarm> findByMemberOrderByCreatedAtDesc(Member member);

    Page<MemberPushAlarm> findByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);

    void deleteAllByMember(Member member);

}
