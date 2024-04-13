package net.l3mon.LogisticsL3mon.voiceChat.controller;

import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.voiceChat.service.VoiceChatService;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
@RequiredArgsConstructor
public class VoiceChatWSController {

    private final VoiceChatService voiceChatService;
//    @PostMapping("/{roomId}/join")
    @MessageMapping("/ws/voiceChat/{voiceChatId}/join")
    public ResponseEntity<String> joinRoom(@DestinationVariable Long voiceChatId, Authentication authentication) {
        try {
            voiceChatService.joinRoom(voiceChatId, authentication);
            return ResponseEntity.ok().body("User joined the room successfully.");
        } catch (GlobalExceptionMessage ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
//        Authentication authentication = (Authentication) event.getUser();
//        if (authentication != null) {
//            Long voiceChatId = voiceChatService.findVoiceChatIdByUser(authentication.getName());
//            if (voiceChatId != null) {
//                messagingTemplate.convertAndSend("/topic/voiceChat/" + voiceChatId + "/info", "User disconnected.");
//            }
//        }
        try {
            voiceChatService.disconected(event);
//            return ResponseEntity.ok().body("User left.");
        } catch (GlobalExceptionMessage ex) {
//            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

//    @MessageMapping("/audio/{roomId}")
//    public void sendAudioStream(@DestinationVariable String roomId, byte[] audioData) {
////        this.messagingTemplate.convertAndSend("/topic/audio/" + roomId, audioData);
//    }
}
