package com.bookbla.americano.domain.chat.repository.custom;

public interface ChatRepositoryCustom {
    void updateIsReadByChatRoomId(Long chatRoomId, Long MemberId, Boolean isRead);
}
