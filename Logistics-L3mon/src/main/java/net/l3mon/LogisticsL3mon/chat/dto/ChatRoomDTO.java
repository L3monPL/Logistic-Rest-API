package net.l3mon.LogisticsL3mon.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoomDTO {
    private Long userId;
    private String message;
    private Long fileId;
    private Long replyToId;
}
