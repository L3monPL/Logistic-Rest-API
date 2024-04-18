package net.l3mon.LogisticsL3mon.file.dto;

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
    private byte[] data;
    private String createdAt;
}
