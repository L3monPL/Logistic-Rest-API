package net.l3mon.LogisticsL3mon.file.service;

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
import net.l3mon.LogisticsL3mon.file.dto.FileDTO;
import net.l3mon.LogisticsL3mon.file.dto.FileWithMessageDTO;
import net.l3mon.LogisticsL3mon.file.entity.File;
import net.l3mon.LogisticsL3mon.file.repository.FileRepository;
import net.l3mon.LogisticsL3mon.room.entity.Room;
import net.l3mon.LogisticsL3mon.room.repository.RoomRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final CompanyUserRepository companyUserRepository;
    private final FileRepository fileRepository;
    private final ChatRoomRepository chatRoomRepository;

    private boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }
    public void uploadFile(Long roomId, MultipartFile multipartFile, String message, SimpMessagingTemplate messagingTemplate) throws GlobalExceptionMessage, IOException {

        if (multipartFile.getSize() > 20 * 1024 * 1024) { // Sprawdź rozmiar pliku
            throw new GlobalExceptionMessage("File exceeding the 20MB limit");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findUserByLogin(username).orElse(null);
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

        File fileDTO = new File();

        if (isImage(multipartFile)){
            try {
                BufferedImage originalImage = ImageIO.read(multipartFile.getInputStream());

                fileDTO.setFilename(multipartFile.getOriginalFilename());
                fileDTO.setWidth(originalImage.getWidth());
                fileDTO.setHeight(originalImage.getHeight());
                fileDTO.setData(multipartFile.getBytes());
                fileDTO.setSize(multipartFile.getSize());
                fileDTO.setType(multipartFile.getContentType());
                fileDTO.setCreatedAt(String.valueOf(LocalDateTime.now()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (!isImage(multipartFile)){
            try {

                fileDTO.setFilename(multipartFile.getOriginalFilename());
                fileDTO.setData(multipartFile.getBytes());
                fileDTO.setSize(multipartFile.getSize());
                fileDTO.setType(multipartFile.getContentType());
                fileDTO.setCreatedAt(String.valueOf(LocalDateTime.now()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File savedFile;
        try {
            savedFile = fileRepository.save(fileDTO);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setRoomId(roomId);
        chatRoom.setUserId(user.getId());
        chatRoom.setMessage(message);
        chatRoom.setFileId(savedFile.getId());
        chatRoom.setEdited(false);
        chatRoom.setCreatedAt(String.valueOf(LocalDateTime.now()));

        try {
            chatRoomRepository.save(chatRoom);
        } catch (GlobalExceptionMessage ex) {
            fileRepository.delete(fileDTO);
            throw new GlobalExceptionMessage("Error: " + ex.getMessage());
        }

        ChatMessageWithFileDTO chatMessageWithFileDTO = new ChatMessageWithFileDTO();

        chatMessageWithFileDTO.setRoomId(roomId);
        chatMessageWithFileDTO.setUserId(user.getId());
        chatMessageWithFileDTO.setMessage(message);
        chatMessageWithFileDTO.setFileId(savedFile.getId());
        chatMessageWithFileDTO.setImageWidth(savedFile.getWidth());
        chatMessageWithFileDTO.setImageHeight(savedFile.getHeight());
        chatMessageWithFileDTO.setType(savedFile.getType());
        chatMessageWithFileDTO.setFileName(savedFile.getFilename());
//        chatMessageWithFileDTO.setReplyToId(chatRoomDTO.getReplyToId());
        chatMessageWithFileDTO.setEdited(false);
        chatMessageWithFileDTO.setCreatedAt(String.valueOf(LocalDateTime.now()));

        messagingTemplate.convertAndSend("/topic/room/" + roomId, chatMessageWithFileDTO);

//        return
    }

    public static boolean isImage(MultipartFile file) {
        // Pobranie MIME type z pliku
        String contentType = file.getContentType();

        // Sprawdzenie, czy MIME type wskazuje na obraz
        return contentType != null && contentType.startsWith("image");
    }

//    public static boolean isImage(String mimeType) {
//        return mimeType != null && (mimeType.equals("image/jpeg") || mimeType.equals("image/png") || mimeType.equals("image/gif") || mimeType.equals("image/bmp") || mimeType.equals("image/svg"));
//    }

    public File getFileById(Long fileId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findUserByLogin(username).orElse(null);
        if (user == null) {
            throw new GlobalExceptionMessage("User not found with username: " + username);
        }

        ChatRoom chatRoom;
        try {
            chatRoom = chatRoomRepository.findByFileId(fileId).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("error: " + ex.getMessage());
        }
        if (chatRoom == null) {
            throw new GlobalExceptionMessage("Doesn't exist");
        }

        Room room;
        try {
            room = roomRepository.findById(chatRoom.getRoomId()).orElse(null);
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

        File file;
        try {
            file = fileRepository.findById(fileId).orElse(null);
        } catch (GlobalExceptionMessage ex) {
            throw new GlobalExceptionMessage("error: " + ex.getMessage());
        }
        if (file == null) {
            throw new GlobalExceptionMessage("Doesn't exist");
        }

        return file;
    }
}
