package com.bookbla.americano.domain.notification.event;

import com.bookbla.americano.domain.notification.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class PushAlarmEventHandler {

    private final AlarmService alarmService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendPostcard(PostcardAlarmEvent postcardAlarmEvent) {
        alarmService.sendPushAlarmForReceivePostCard(postcardAlarmEvent.getSendMember(), postcardAlarmEvent.getReceiveMember());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void acceptPostcard(PostcardAlarmEvent postcardAlarmEvent) {
        alarmService.sendPushAlarmForAcceptPostcard(postcardAlarmEvent.getSendMember(), postcardAlarmEvent.getReceiveMember());
    }
}
