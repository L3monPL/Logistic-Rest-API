package net.l3mon.LogisticsL3mon.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.l3mon.LogisticsL3mon.file.entity.File;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileWithMessageDTO {
    private Long id;
    private Long roomId;
    private Long userId;
    private String message;
    private Long fileId;
    private File file;
    private Long replyToId;
    private boolean isEdited;
    private String createdAt;
}
