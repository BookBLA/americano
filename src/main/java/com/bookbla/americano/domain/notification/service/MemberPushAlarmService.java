package com.bookbla.americano.domain.notification.service;

import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmSettingCreateRequest;
import com.bookbla.americano.domain.notification.controller.dto.response.PushAlarmSettingResponse;
import java.util.List;

import com.bookbla.americano.domain.member.repository.MemberPushAlarmRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import com.bookbla.americano.domain.notification.controller.dto.response.MemberPushAlarmReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberPushAlarmService {

    private final MemberRepository memberRepository;
    private final MemberPushAlarmRepository memberPushAlarmRepository;

    @Transactional(readOnly = true)
    public MemberPushAlarmReadResponse readPushAlarm(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<MemberPushAlarm> memberPushAlarms = memberPushAlarmRepository.findByMemberOrderByCreatedAtDesc(member);
        return MemberPushAlarmReadResponse.from(member, memberPushAlarms);
    }

    @Transactional
    public void deletePushAlarm(Long memberId, Long memberPushAlarmId) {
        memberPushAlarmRepository.findById(memberPushAlarmId)
            .ifPresent(memberPushAlarm -> memberPushAlarm.validateOwner(memberId));

        memberPushAlarmRepository.deleteById(memberPushAlarmId);
    }

    @Transactional
    public void deleteAllPushAlarm(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        memberPushAlarmRepository.deleteAllByMember(member);
    }

    @Transactional
    public PushAlarmSettingResponse updatePushAlarmSetting(Long memberId,
        PushAlarmSettingCreateRequest request) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        member.updatePushTokenEnabled(request.getPushAlarmEnabled());
        return PushAlarmSettingResponse.from(member);
    }

    public PushAlarmSettingResponse getPushAlarmSetting(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return PushAlarmSettingResponse.from(member);
    }
}