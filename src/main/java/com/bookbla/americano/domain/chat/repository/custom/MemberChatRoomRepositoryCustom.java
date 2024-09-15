package com.bookbla.americano.domain.chat.repository.custom;

import com.bookbla.americano.domain.chat.repository.entity.MemberChatRoom;

import java.util.List;

public interface MemberChatRoomRepositoryCustom {
    List<MemberChatRoom> findByMemberIdAndPostcardId(Long memberId, Long postcardId);
}
