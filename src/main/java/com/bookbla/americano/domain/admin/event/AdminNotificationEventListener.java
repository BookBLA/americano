package com.bookbla.americano.domain.admin.event;

import com.bookbla.americano.domain.admin.service.AdminNotificationClient;
import com.bookbla.americano.domain.member.service.dto.event.AdminNotificationEventWithAfterCommit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class AdminNotificationEventListener {

    private final AdminNotificationClient adminNotificationClient;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMessageAfterCommit(AdminNotificationEventWithAfterCommit adminNotificationEvent) {
        adminNotificationClient.send(adminNotificationEvent.getTitle(), adminNotificationEvent.getContents());
    }

}
