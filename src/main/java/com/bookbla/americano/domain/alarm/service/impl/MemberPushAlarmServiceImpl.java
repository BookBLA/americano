package com.bookbla.americano.domain.alarm.service.impl;

import java.util.List;

import com.bookbla.americano.domain.alarm.controller.dto.response.MemberPushAlarmResponse;
import com.bookbla.americano.domain.alarm.service.MemberPushAlarmService;
import com.bookbla.americano.domain.member.repository.MemberPushAlarmRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberPushAlarmServiceImpl implements MemberPushAlarmService {

    private final MemberRepository memberRepository;
    private final MemberPushAlarmRepository memberPushAlarmRepository;

    @Override
    @Transactional(readOnly = true)
    public MemberPushAlarmResponse readPushAlarm(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<MemberPushAlarm> memberPushAlarms = memberPushAlarmRepository.findByMember(member);
        return MemberPushAlarmResponse.from(member, memberPushAlarms);
    }

    @Override
    @Transactional
    public void deletePushAlarm(Long memberId, Long memberPushAlarmId) {
        MemberPushAlarm memberPushAlarm = memberPushAlarmRepository.getById(memberPushAlarmId);
        memberPushAlarm.validateOwner(memberId);

        memberPushAlarmRepository.deleteById(memberPushAlarmId);
    }

    @Override
    @Transactional
    public void deleteAllPushAlarm(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        memberPushAlarmRepository.deleteAllByMember(member);
    }
}
