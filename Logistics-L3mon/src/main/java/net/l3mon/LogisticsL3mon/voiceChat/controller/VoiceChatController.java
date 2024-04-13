package net.l3mon.LogisticsL3mon.voiceChat.controller;

import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.Server.ErrorResponse;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.voiceChat.entity.VoiceChat;
import net.l3mon.LogisticsL3mon.voiceChat.service.VoiceChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/voiceChat")
@RequiredArgsConstructor
public class VoiceChatController {

    private final VoiceChatService voiceChatService;

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<?> createVoiceChat(@RequestBody VoiceChat voiceChat){
        try {
            return ResponseEntity.ok(voiceChatService.createVoiceChat(voiceChat));
        } catch (GlobalExceptionMessage ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
