package net.l3mon.LogisticsL3mon.room.service;

import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.room.dto.RoomDTO;
import net.l3mon.LogisticsL3mon.room.entity.Room;
import net.l3mon.LogisticsL3mon.room.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    private Room saveRoom(Room room){
        return roomRepository.saveAndFlush(room);
    }

    public void createByUser(RoomDTO roomDTO) {
        Room room = new Room();
        room.setCompanyId(roomDTO.getCompanyId());
        room.setName(roomDTO.getName());
        room.setPermissionForAll(false);
        room.setCreatedAt(String.valueOf(LocalDateTime.now()));

        saveRoom(room);
    }

    public List<Room> getAllRooms(){
        return roomRepository.findAll();
    }
}
