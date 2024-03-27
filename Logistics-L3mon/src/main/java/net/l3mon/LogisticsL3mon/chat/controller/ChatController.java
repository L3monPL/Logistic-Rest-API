package net.l3mon.LogisticsL3mon.chat.controller;

import net.l3mon.LogisticsL3mon.chat.dto.ChatMessage;
import net.l3mon.LogisticsL3mon.chat.dto.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/chat/sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }

//    @MessageMapping("/chat.addUser")
//    @SendTo("/topic/public")
//    public ChatMessage addUser(ChatMessage chatMessage) {
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.message")
//    @SendTo("/topic/messages")
//    public Message sendMessage2(Message message) {
//        // Tutaj możesz przetwarzać otrzymane wiadomości i zwracać odpowiedzi
//        return message;
//    }

}
