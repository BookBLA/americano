package com.bookbla.americano.domain.chat.controller.dto;

import com.bookbla.americano.domain.chat.enums.RoomConnectionStatus;
import lombok.Getter;

@Getter
public class RoomConnection {

    Long memberId;

    RoomConnectionStatus status;
}
