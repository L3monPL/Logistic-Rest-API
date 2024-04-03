package net.l3mon.LogisticsL3mon.chat.service;

import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.UserAuth.entity.User;
import net.l3mon.LogisticsL3mon.UserAuth.repository.UserRepository;
import net.l3mon.LogisticsL3mon.chat.dto.ChatMessage;
import net.l3mon.LogisticsL3mon.chat.dto.ChatRoomDTO;
import net.l3mon.LogisticsL3mon.chat.entity.ChatRoom;
import net.l3mon.LogisticsL3mon.chat.repository.ChatRoomRepository;
import net.l3mon.LogisticsL3mon.company.entity.CompanyUser;
import net.l3mon.LogisticsL3mon.company.repository.CompanyUserRepository;
import net.l3mon.LogisticsL3mon.room.entity.Room;
import net.l3mon.LogisticsL3mon.room.repository.RoomRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final RoomRepository roomRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final CompanyUserRepository companyUserRepository;

    public ChatRoom sendMessageToRoomId(Long roomId, ChatRoomDTO chatRoomDTO, Authentication authentication) throws GlobalExceptionMessage {
        String username = authentication.getName();

        User user;
        try {
            user = userRepository.findUserByLogin(username).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("error: " + ex.getMessage());
        }
        if (user == null) {
            throw new GlobalExceptionMessage("User not found with username: " + username);
        }

        Room room;
        try {
            room = roomRepository.findById(roomId).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("error: " + ex.getMessage());
        }
        if (room == null) {
            throw new GlobalExceptionMessage("Such a room does not exist");
        }

        CompanyUser companyUser;
        try {
            companyUser = companyUserRepository.findByUserIdAndCompanyId(user.getId(), room.getCompanyId()).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }
        if (companyUser == null) {
            throw new GlobalExceptionMessage("Don't have permission");
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);
        chatRoom.setUserId(chatRoomDTO.getUserId());
        chatRoom.setMessage(chatRoomDTO.getMessage());
        chatRoom.setFileId(chatRoomDTO.getFileId());
        chatRoom.setReplyToId(chatRoomDTO.getReplyToId());
        chatRoom.setEdited(false);
        chatRoom.setCreatedAt(String.valueOf(LocalDateTime.now()));

        try {
            chatRoomRepository.save(chatRoom);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }

        return chatRoom;
    }

    public void connectToRoom(Long roomId, Authentication authentication, SimpMessagingTemplate messagingTemplate) {
        String username = authentication.getName();

        User user;
        try {
            user = userRepository.findUserByLogin(username).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("error: " + ex.getMessage());
        }
        if (user == null) {
            throw new GlobalExceptionMessage("User not found with username: " + username);
        }

        List<ChatRoom> messages = chatRoomRepository.findByRoomId(roomId);

        messagingTemplate.convertAndSendToUser(username, "/topic/room/" + roomId, messages);
    }
}
