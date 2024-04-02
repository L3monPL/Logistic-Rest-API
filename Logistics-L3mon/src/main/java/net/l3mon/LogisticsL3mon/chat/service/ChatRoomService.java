package net.l3mon.LogisticsL3mon.chat.service;

import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.room.repository.RoomRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final RoomRepository roomRepository;
}
