package net.l3mon.LogisticsL3mon.chat.controller;

import lombok.RequiredArgsConstructor;
import net.l3mon.LogisticsL3mon.Server.ErrorResponse;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.chat.dto.ChatRoomDTO;
import net.l3mon.LogisticsL3mon.chat.service.ChatRoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ChatRoomWSController {

    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/ws/room/{roomId}/sendMessage")
    @SendTo("/topic/room/{roomId}")
    public Object sendMessage(@DestinationVariable Long roomId, @Payload ChatRoomDTO chatRoomDTO, Authentication authentication) {
        try {
//            return ResponseEntity.ok(chatRoomService.sendMessageToRoomId(roomId, chatRoomDTO, authentication));
            return chatRoomService.sendMessageToRoomId(roomId, chatRoomDTO, authentication);
        } catch (GlobalExceptionMessage ex) {
            return new ErrorResponse(ex.getMessage());
        }
    }
//    @SubscribeMapping("/room/{roomId}/connect")
    @MessageMapping("/ws/room/{roomId}/message")
    public void connectToRoom(@DestinationVariable Long roomId, Authentication authentication, @Payload int page) {
        try {
            chatRoomService.connectToRoom(roomId, authentication, messagingTemplate, page, 25);
        } catch (GlobalExceptionMessage ex) {
//            return new ErrorResponse(ex.getMessage());
        }
    }

}
