package net.l3mon.LogisticsL3mon.room.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoomDTO {
    private Long id;
    private Long companyId;
    private String name;
    private boolean permissionForAll;
}
