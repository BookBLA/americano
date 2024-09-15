package com.bookbla.americano.domain.chat.repository;

import com.bookbla.americano.domain.chat.repository.entity.MemberChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberChatRoomRepository extends JpaRepository<MemberChatRoom, Long> {
    Optional<MemberChatRoom> findByMember_IdAndChatRoom_Id(Long memberId, Long chatRoomId);

}
