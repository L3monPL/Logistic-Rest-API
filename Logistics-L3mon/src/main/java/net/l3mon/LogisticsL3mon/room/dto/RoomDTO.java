package net.l3mon.LogisticsL3mon.room.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Long companyId;
    private String name;
    private boolean permissionForAll;
}
