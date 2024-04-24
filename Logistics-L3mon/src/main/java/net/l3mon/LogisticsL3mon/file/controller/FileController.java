package net.l3mon.LogisticsL3mon.file.controller;

import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.Server.ErrorResponse;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.file.entity.File;
import net.l3mon.LogisticsL3mon.file.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ErrorResponse> uploadFile(@PathVariable Long roomId, @RequestPart(value = "file") MultipartFile file, @RequestParam(value = "message", required = false) String message) {
        try {
            fileService.uploadFile(roomId, file, message, messagingTemplate);
        } catch (GlobalExceptionMessage ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (IOException ex){
            throw new RuntimeException(ex);
        }
        return null;
    }

    @RequestMapping(path = "/{fileId}", method = RequestMethod.GET)
    public ResponseEntity<?> getRoomsListByCompanyId(@PathVariable Long fileId) {
        File file;
        try {
            file = fileService.getFileById(fileId);
        } catch (GlobalExceptionMessage ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.ok(file);
    }
}
