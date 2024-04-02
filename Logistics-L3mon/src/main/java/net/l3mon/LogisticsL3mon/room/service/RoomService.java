package net.l3mon.LogisticsL3mon.room.service;

import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.Server.ErrorResponse;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.UserAuth.entity.User;
import net.l3mon.LogisticsL3mon.UserAuth.repository.UserRepository;
import net.l3mon.LogisticsL3mon.company.entity.CompanyInviteLink;
import net.l3mon.LogisticsL3mon.company.entity.CompanyUser;
import net.l3mon.LogisticsL3mon.company.repository.CompanyUserRepository;
import net.l3mon.LogisticsL3mon.room.dto.RoomDTO;
import net.l3mon.LogisticsL3mon.room.entity.Room;
import net.l3mon.LogisticsL3mon.room.repository.RoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final CompanyUserRepository companyUserRepository;

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

    public List<Room> getRoomsListByCompanyId(Long companyId) throws GlobalExceptionMessage{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findUserByLogin(username).orElse(null);
        if (user == null) {
            throw new GlobalExceptionMessage("User not found with username: " + username);
        }

        CompanyUser companyUser = companyUserRepository.findByUserIdAndCompanyId(user.getId(), companyId).orElse(null);
        if (companyUser == null) {
            throw new GlobalExceptionMessage("Permission fail");
        }

        List<Room> rooms;
        try {
            rooms = roomRepository.findByCompanyId(companyId);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("error: " + ex.getMessage());
        }

        return rooms;
    }
}
