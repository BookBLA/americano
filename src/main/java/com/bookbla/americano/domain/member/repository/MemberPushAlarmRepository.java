package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import com.bookbla.americano.domain.member.repository.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPushAlarmRepository extends JpaRepository<MemberPushAlarm, Long> {

    List<MemberPushAlarm> findByMember(Member member);

    void deleteAllByMember(Member member);

}
