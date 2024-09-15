package com.bookbla.americano.domain.chat.repository.custom;

import com.bookbla.americano.domain.chat.controller.dto.ChatRoomResponse;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoomResponse> findByMemberId(Long memberId);
}
