package com.bookbla.americano.domain.member.repository;

import com.bookbla.americano.base.exception.BaseException;
import com.bookbla.americano.domain.alarm.exception.MemberPushAlarmExceptionType;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import com.bookbla.americano.domain.member.repository.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPushAlarmRepository extends JpaRepository<MemberPushAlarm, Long> {

    default MemberPushAlarm getById(Long memberPushAlarmId) {
        return findById(memberPushAlarmId)
                .orElseThrow(() -> new BaseException(MemberPushAlarmExceptionType.ID_NOT_FOUND));
    }

    List<MemberPushAlarm> findByMember(Member member);

    void deleteAllByMember(Member member);

}
