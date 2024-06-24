package com.bookbla.americano.base.log.discord;

import com.bookbla.americano.domain.scheduler.dto.AdminLogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class DiscordLogEventListener {

    private final BookblaLogDiscord bookblaLogDiscord;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendAfterCommit(AdminLogEvent adminLogEvent) {
        bookblaLogDiscord.sendMessage(adminLogEvent.getMessage());
    }

}
