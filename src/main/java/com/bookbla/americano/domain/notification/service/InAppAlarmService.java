package com.bookbla.americano.domain.notification.service;

import com.bookbla.americano.domain.chat.repository.entity.Chat;
import com.bookbla.americano.domain.member.repository.entity.Member;
import com.bookbla.americano.domain.notification.controller.dto.request.InAppAlarmRequest;
import com.bookbla.americano.domain.notification.controller.dto.response.InAppAlarmResponse;
import com.bookbla.americano.domain.notification.enums.InAppAlarmStatus;
import com.bookbla.americano.domain.notification.repository.InAppAlarmLogRepository;
import com.bookbla.americano.domain.notification.repository.entity.InAppAlarmLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InAppAlarmService {
    private final SimpUserRegistry userRegistry;
    private final SimpMessagingTemplate messagingTemplate;
    private final EntityManager em;
    private final InAppAlarmLogRepository inAppAlarmLogRepository;


    public Boolean isMemberInApp(Long memberId) {
        return !userRegistry.findSubscriptions(subscription ->
                        subscription.getDestination().equals("/topic/alarm/"+memberId))
                .isEmpty();
    }

    public void sendChatInAppAlarm(Chat chat, Long memberId) throws Exception {
        InAppAlarmRequest req = InAppAlarmRequest.builder()
                .body(chat.getContent())
                .title(chat.getSender().getMemberProfile().getName())
                .imageUrl(chat.getSender().getMemberStyle().getProfileImageType().getImageUrl())
                .build();
        sendInAppAlarm(req, memberId);
    }

    public void sendInAppAlarm(InAppAlarmRequest req, Long memberId) throws Exception {
        InAppAlarmLog inAppAlarmLog = InAppAlarmLog.from(req, em.getReference(Member.class, memberId));
        if (!isMemberInApp(memberId)) {
            log.error("sendInAppAlarm : 유저가 접속해 있지 않습니다. id: " + memberId);
            inAppAlarmLog.setStatus(InAppAlarmStatus.FAIL);
            inAppAlarmLogRepository.save(inAppAlarmLog);
            throw new Exception();
        }

        messagingTemplate.convertAndSend("/topic/alarm/"+memberId, InAppAlarmResponse.of(req));
        inAppAlarmLog.setStatus(InAppAlarmStatus.DONE);
        inAppAlarmLogRepository.save(inAppAlarmLog);

    }
}
