package com.bookbla.americano.domain.admin.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.admin.service.dto.AlarmDto;
import com.bookbla.americano.domain.alarm.service.AlarmClient;
import com.bookbla.americano.domain.alarm.service.AlarmService;
import com.bookbla.americano.domain.member.enums.MemberStatus;
import com.bookbla.americano.domain.member.repository.MemberPushAlarmRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
public class AdminMarketingService {

    private final AlarmClient alarmClient;
    private final MemberRepository memberRepository;
    private final MemberPushAlarmRepository memberPushAlarmRepository;
    private final TransactionTemplate transactionTemplate;

    public void sendPushAlarm(AlarmDto alarmDto) {
        List<Member> members = memberRepository.findByMemberStatus(MemberStatus.COMPLETED, MemberStatus.MATCHING_DISABLED);
        Map<Member, String> sendableMemberTokenMap = members.stream()
                .filter(Member::canSendAdvertisementAlarm)
                .collect(Collectors.toMap(
                        Function.identity(),
                        Member::getPushToken
                ));

        alarmClient.sendAll(toTokens(sendableMemberTokenMap), alarmDto.getTitle(), alarmDto.getContents());
        transactionTemplate.executeWithoutResult(action ->
                memberPushAlarmRepository.saveAllAndFlush(toMemberPushAlarms(alarmDto, sendableMemberTokenMap))
        );
    }

    private List<String> toTokens(Map<Member, String> memberTokens) {
        return memberTokens.keySet()
                .stream()
                .map(memberTokens::get)
                .collect(Collectors.toList());
    }

    private List<MemberPushAlarm> toMemberPushAlarms(AlarmDto alarmDto, Map<Member, String> memberTokenMap) {
        return memberTokenMap.keySet()
                .stream()
                .map(member -> MemberPushAlarm.builder()
                        .body(alarmDto.getContents())
                        .title(alarmDto.getTitle())
                        .member(member)
                        .build())
                .collect(Collectors.toList());
    }

}
