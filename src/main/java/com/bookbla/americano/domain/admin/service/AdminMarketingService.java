package com.bookbla.americano.domain.admin.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bookbla.americano.domain.admin.service.dto.NotificationDto;
import com.bookbla.americano.domain.notification.service.NotificationClient;
import com.bookbla.americano.domain.member.repository.MemberPushAlarmRepository;
import com.bookbla.americano.domain.member.repository.MemberRepository;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.member.repository.entity.MemberPushAlarm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import static com.bookbla.americano.domain.member.enums.MemberStatus.*;


@RequiredArgsConstructor
@Service
public class AdminMarketingService {

    private final NotificationClient notificationClient;
    private final MemberRepository memberRepository;
    private final MemberPushAlarmRepository memberPushAlarmRepository;
    private final TransactionTemplate transactionTemplate;

    public void sendNotification(NotificationDto notificationDto, Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        notificationClient.send(member.getPushToken(), notificationDto.getTitle(), notificationDto.getContents());

        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(member)
                .title(notificationDto.getTitle())
                .body(notificationDto.getContents())
                .build();
        transactionTemplate.executeWithoutResult(action -> memberPushAlarmRepository.save(memberPushAlarm));
    }

    // TODO: 회원 별 성공/실패 결과 전달할 방법?
    // TODO: expo는 토큰 보낸 순서대로 data를 보내줌
    public void sendNotifications(NotificationDto notificationDto) {
        List<Member> members = memberRepository.findByMemberStatus(COMPLETED, MATCHING_DISABLED);
        Map<Member, String> sendableMemberTokenMap = members.stream()
                .filter(Member::canSendAdvertisementAlarm)
                .collect(Collectors.toMap(
                        Function.identity(),
                        Member::getPushToken
                ));

        notificationClient.sendAll(toTokens(sendableMemberTokenMap), notificationDto.getTitle(), notificationDto.getContents());
        transactionTemplate.executeWithoutResult(action ->
                memberPushAlarmRepository.saveAllAndFlush(toMemberPushAlarms(notificationDto, sendableMemberTokenMap))
        );
    }

    private List<String> toTokens(Map<Member, String> memberTokens) {
        return memberTokens.keySet()
                .stream()
                .map(memberTokens::get)
                .collect(Collectors.toList());
    }

    private List<MemberPushAlarm> toMemberPushAlarms(NotificationDto notificationDto, Map<Member, String> memberTokenMap) {
        return memberTokenMap.keySet()
                .stream()
                .map(member -> MemberPushAlarm.builder()
                        .body(notificationDto.getContents())
                        .title(notificationDto.getTitle())
                        .member(member)
                        .build())
                .collect(Collectors.toList());
    }

}
