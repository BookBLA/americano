package com.bookbla.americano.domain.chat.repository;

import com.bookbla.americano.domain.chat.repository.custom.ChatRoomRepositoryCustom;
import com.bookbla.americano.domain.chat.repository.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {

    @Transactional
    @Modifying
    @Query("update ChatRoom c set c.isAlert = ?2 where c.id = ?1")
    void updateIsAlert(Long id, Boolean isAlert);


}
