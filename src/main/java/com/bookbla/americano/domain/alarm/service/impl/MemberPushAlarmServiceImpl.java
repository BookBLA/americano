package com.bookbla.americano.domain.alarm.service.impl;

import com.bookbla.americano.domain.alarm.controller.dto.response.MemberPushAlarmAllDeleteResponse;
import com.bookbla.americano.domain.alarm.controller.dto.response.MemberPushAlarmDeleteResponse;
import com.bookbla.americano.domain.alarm.controller.dto.response.MemberPushAlarmResponse;
import com.bookbla.americano.domain.member.repository.MemberPushAlarmRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import com.bookbla.americano.domain.alarm.service.MemberPushAlarmService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberPushAlarmServiceImpl implements MemberPushAlarmService {

    private final MemberRepository memberRepository;
    private final MemberPushAlarmRepository memberPushAlarmRepository;

    @Override
    public MemberPushAlarmResponse readPushAlarm(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<MemberPushAlarm> memberPushAlarms = memberPushAlarmRepository.findByMember(member);
        return MemberPushAlarmResponse.from(member, memberPushAlarms);
    }

    @Override
    @Transactional
    public MemberPushAlarmDeleteResponse deletePushAlarm(Long memberId, Long memberPushAlarmId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        memberPushAlarmRepository.deleteById(memberPushAlarmId);
        return MemberPushAlarmDeleteResponse.from(member, memberPushAlarmId);
    }

    @Override
    @Transactional
    public MemberPushAlarmAllDeleteResponse deleteAllPushAlarm(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        memberPushAlarmRepository.deleteAllByMember(member);
        return MemberPushAlarmAllDeleteResponse.from(member);
    }

}
