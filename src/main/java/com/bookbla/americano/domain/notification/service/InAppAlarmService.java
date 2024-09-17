package com.bookbla.americano.domain.notification.service;

import com.bookbla.americano.domain.notification.controller.dto.request.InAppAlarmRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InAppAlarmService {

    private final SimpUserRegistry userRegistry;

    private final SimpMessagingTemplate messagingTemplate;

}
