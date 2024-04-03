package net.l3mon.LogisticsL3mon.chat.repository;

import net.l3mon.LogisticsL3mon.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByRoomId(Long roomId);
}
