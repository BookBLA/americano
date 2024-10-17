package com.bookbla.americano.domain.notification.service;

import com.bookbla.americano.domain.notification.controller.dto.request.PushAlarmSettingCreateRequest;
import com.bookbla.americano.domain.notification.controller.dto.response.MemberPushAlarmReadResponses;
import com.bookbla.americano.domain.notification.controller.dto.response.PushAlarmSettingResponse;
import java.util.List;

import com.bookbla.americano.domain.member.repository.MemberPushAlarmRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import com.bookbla.americano.domain.notification.controller.dto.response.MemberPushAlarmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberPushAlarmService {

    private final MemberRepository memberRepository;
    private final MemberPushAlarmRepository memberPushAlarmRepository;

    @Transactional(readOnly = true)
    public MemberPushAlarmReadResponses readPushAlarms(Long memberId, Pageable pageable) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        Page<MemberPushAlarm> memberPushAlarmPaging = memberPushAlarmRepository.findByMemberOrderByCreatedAtDesc(member, pageable);
        List<MemberPushAlarm> memberPushAlarms = memberPushAlarmPaging.getContent();
        return MemberPushAlarmReadResponses.from(memberPushAlarms);
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

    @Transactional
    public PushAlarmSettingResponse getPushAlarmSetting(Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        return PushAlarmSettingResponse.from(member);
    }
}