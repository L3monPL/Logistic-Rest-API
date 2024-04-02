package net.l3mon.LogisticsL3mon.chat.repository;

import net.l3mon.LogisticsL3mon.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
