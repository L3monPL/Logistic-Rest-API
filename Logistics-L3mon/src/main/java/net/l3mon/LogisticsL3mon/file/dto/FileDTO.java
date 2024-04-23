package net.l3mon.LogisticsL3mon.file.dto;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private String filename;
    private Long size;
    private String type;
    private String width;
    private String height;
    private byte[] data;
    private String createdAt;
}
