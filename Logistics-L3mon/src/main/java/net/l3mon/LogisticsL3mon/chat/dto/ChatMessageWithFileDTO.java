package net.l3mon.LogisticsL3mon.chat.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.l3mon.LogisticsL3mon.file.entity.File;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageWithFileDTO {
    private Long id;
    private Long roomId;
    private Long userId;
    private String message;
    private Long fileId;
    private String type;
    private String fileName;
    private int imageWidth;
    private int imageHeight;
    private Long replyToId;
    private boolean isEdited;
    private String createdAt;
}
