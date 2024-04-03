package net.l3mon.LogisticsL3mon.chat.controller;

import net.l3mon.LogisticsL3mon.chat.dto.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
public class ChatRoomController {

    @MessageMapping("/ws/room/{roomId}/sendMessage")
    @SendTo("/topic/room/{roomId}")
    public ChatMessage sendMessage(ChatMessage message, Authentication authentication) {

        String username = authentication.getName();
        System.out.println(username);
        System.out.println(message.getContent());

        return message;
    }
}
