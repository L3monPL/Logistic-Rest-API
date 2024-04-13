package net.l3mon.LogisticsL3mon.voiceChat.service;

import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.UserAuth.entity.User;
import net.l3mon.LogisticsL3mon.UserAuth.repository.UserRepository;
import net.l3mon.LogisticsL3mon.voiceChat.entity.VoiceChat;
import net.l3mon.LogisticsL3mon.voiceChat.repository.VoiceChatRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Service
@RequiredArgsConstructor
public class VoiceChatService {

    private final VoiceChatRepository voiceChatRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public VoiceChat createVoiceChat(VoiceChat voiceChat) {

        VoiceChat voiceChatCreate = new VoiceChat();
        voiceChatCreate.setChatName(voiceChat.getChatName());
        voiceChatCreate.setCompanyId(voiceChat.getCompanyId());
        return voiceChatRepository.save(voiceChatCreate);
    }

    public void joinRoom(Long voiceChatId, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findUserByLogin(username)
                .orElseThrow(() -> new GlobalExceptionMessage("User not found with username: " + username));

        VoiceChat voiceChat = voiceChatRepository.findById(voiceChatId)
                .orElseThrow(() -> new GlobalExceptionMessage("Room not found with id: " + voiceChatId));

        System.out.println(voiceChatId);
        messagingTemplate.convertAndSend("/topic/voiceChat/" + voiceChatId,
                user);
    }

    public void disconected(SessionDisconnectEvent event) {

        Authentication authentication = (Authentication) event.getUser();

        System.out.println("disconedted: " + authentication.getName());

        if (authentication != null) {
            User user = userRepository.findUserByLogin(authentication.getName())
                    .orElseThrow(() -> new GlobalExceptionMessage("User not found with username: " + authentication.getName()));
            
        }
    }
}
