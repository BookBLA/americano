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
import com.bookbla.americano.domain.notification.service.dto.NotificationResponse;
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

    public List<NotificationResponse> sendNotification(NotificationDto notificationDto, Long memberId) {
        Member member = memberRepository.getByIdOrThrow(memberId);
        List<NotificationResponse> response = notificationClient.send(member.getPushToken(), notificationDto.getTitle(), notificationDto.getContents());

        response.stream()
                .filter(NotificationResponse::isSuccess)
                .forEach(it -> saveMemberPushAlarm(notificationDto, member));
        return response;
    }

    private void saveMemberPushAlarm(NotificationDto notificationDto, Member member) {
        MemberPushAlarm memberPushAlarm = MemberPushAlarm.builder()
                .member(member)
                .title(notificationDto.getTitle())
                .body(notificationDto.getContents())
                .build();
        transactionTemplate.executeWithoutResult(action -> memberPushAlarmRepository.save(memberPushAlarm));
    }

    // TODO: 회원 별 성공/실패 결과 전달할 방법?
    // TODO: expo는 토큰 보낸 순서대로 data를 보내줌
    public List<NotificationResponse> sendNotifications(NotificationDto notificationDto) {
        List<Member> members = memberRepository.findByMemberStatus(COMPLETED, MATCHING_DISABLED);
        Map<Member, String> sendableMemberTokenMap = members.stream()
                .filter(Member::canSendAdvertisementAlarm)
                .collect(Collectors.toMap(
                        Function.identity(),
                        Member::getPushToken
                ));

        List<NotificationResponse> response = notificationClient.sendAll(toTokens(sendableMemberTokenMap), notificationDto.getTitle(), notificationDto.getContents());
        return response;
    }

    private List<String> toTokens(Map<Member, String> memberTokens) {
        return memberTokens.keySet()
                .stream()
                .map(memberTokens::get)
                .collect(Collectors.toList());
    }

}
