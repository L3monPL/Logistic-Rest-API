package net.l3mon.LogisticsL3mon.chat.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChatMessage {
    private String type;
    private String content;
    private String sender;
}
