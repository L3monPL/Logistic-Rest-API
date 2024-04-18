package net.l3mon.LogisticsL3mon.chat.repository;

import net.l3mon.LogisticsL3mon.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Page<ChatRoom> findByRoomId(Long roomId, Pageable pageable);

    double countByRoomId(Long roomId);

    Optional<ChatRoom> findByFileId(Long fileId);

}
