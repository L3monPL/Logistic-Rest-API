package net.l3mon.LogisticsL3mon.file.controller;

import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.Server.ErrorResponse;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.chat.dto.ChatRoomDTO;
import net.l3mon.LogisticsL3mon.file.dto.FileDTO;
import net.l3mon.LogisticsL3mon.file.entity.File;
import net.l3mon.LogisticsL3mon.file.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final SimpMessagingTemplate messagingTemplate;

    @RequestMapping(path = "/{roomId}/upload", method = RequestMethod.POST)
    public void uploadFile(@PathVariable Long roomId, @RequestPart(value = "file") MultipartFile file, @RequestParam("message") String message) {
        try {
            fileService.uploadFile(roomId, file, message, messagingTemplate);
//            return ResponseEntity.ok();
        } catch (GlobalExceptionMessage ex) {
//            return new ErrorResponse(ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
