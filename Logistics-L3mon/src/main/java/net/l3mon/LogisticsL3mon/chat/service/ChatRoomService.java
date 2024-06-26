package net.l3mon.LogisticsL3mon.chat.service;

import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.UserAuth.entity.User;
import net.l3mon.LogisticsL3mon.UserAuth.repository.UserRepository;
import net.l3mon.LogisticsL3mon.chat.dto.ChatMessageWithFileDTO;
import net.l3mon.LogisticsL3mon.chat.dto.ChatRoomDTO;
import net.l3mon.LogisticsL3mon.chat.entity.ChatRoom;
import net.l3mon.LogisticsL3mon.chat.repository.ChatRoomRepository;
import net.l3mon.LogisticsL3mon.company.entity.CompanyUser;
import net.l3mon.LogisticsL3mon.company.repository.CompanyUserRepository;
import net.l3mon.LogisticsL3mon.file.entity.File;
import net.l3mon.LogisticsL3mon.file.repository.FileRepository;
import net.l3mon.LogisticsL3mon.room.entity.Room;
import net.l3mon.LogisticsL3mon.room.repository.RoomRepository;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final RoomRepository roomRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final CompanyUserRepository companyUserRepository;
    private final FileRepository fileRepository;

    public ChatRoom sendMessageToRoomId(Long roomId, ChatRoomDTO chatRoomDTO, Authentication authentication) throws GlobalExceptionMessage {
        String username = authentication.getName();

        System.out.println(username);

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
        chatRoom.setUserId(user.getId());
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

    public void getMessages(Long roomId, Authentication authentication, SimpMessagingTemplate messagingTemplate, int page, int size) {
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

        int totalPages = (int) Math.ceil(chatRoomRepository.countByRoomId(roomId) / (double) size);
        int lastPage = Math.max(0, totalPages - 1);
        int requestedPage = Math.min(page, lastPage);

        Pageable pageable = PageRequest.of(requestedPage, size, Sort.by("createdAt").descending());
        List<ChatRoom> messages = chatRoomRepository.findByRoomId(roomId, pageable).getContent();

        List<ChatRoom> mutableMessages = new ArrayList<>(messages);
        Collections.reverse(mutableMessages);

        List<Object> messagesWithData = new ArrayList<>();

        for (ChatRoom chatRoom: mutableMessages){
            if (chatRoom.getFileId() == null){
                messagesWithData.add(chatRoom);
            }
            if (chatRoom.getFileId() != null){
                File file = fileRepository.findById(chatRoom.getFileId()).orElse(null);
                if (file != null){
//                    if (Objects.equals(file.getType(), "image/jpeg") || Objects.equals(file.getType(), "image/png")){
                    if (isImage(file.getType())){
                        System.out.println(file);

                        ChatMessageWithFileDTO chatMessageWithFileDTO = new ChatMessageWithFileDTO();

                        chatMessageWithFileDTO.setId(chatRoom.getId());
                        chatMessageWithFileDTO.setRoomId(chatRoom.getRoomId());
                        chatMessageWithFileDTO.setUserId(chatRoom.getUserId());
                        chatMessageWithFileDTO.setMessage(chatRoom.getMessage());
                        chatMessageWithFileDTO.setFileId(chatRoom.getFileId());
                        chatMessageWithFileDTO.setFile(file);
                        chatMessageWithFileDTO.setReplyToId(chatRoom.getReplyToId());
                        chatMessageWithFileDTO.setEdited(chatRoom.isEdited());
                        chatMessageWithFileDTO.setCreatedAt(chatRoom.getCreatedAt());

                        messagesWithData.add(chatMessageWithFileDTO);
                    }
//                    }
                }
//                ChatRoom chatRoomWithImage
////                messagesWithData.add(chatRoom);
            }
        }

//        System.out.println(messages);
//        System.out.println(mutableMessages);


        messagingTemplate.convertAndSendToUser(username, "/topic/room/" + roomId, messagesWithData);
    }

    public static boolean isImage(String mimeType) {
        return mimeType != null && (mimeType.equals("image/jpeg") || mimeType.equals("image/png") || mimeType.equals("image/gif") || mimeType.equals("image/bmp") || mimeType.equals("image/svg"));
    }
}
