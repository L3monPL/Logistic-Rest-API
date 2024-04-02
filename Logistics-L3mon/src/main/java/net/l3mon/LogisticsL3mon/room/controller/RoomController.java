package net.l3mon.LogisticsL3mon.room.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.l3mon.LogisticsL3mon.Server.ErrorResponse;
import net.l3mon.LogisticsL3mon.Server.GlobalExceptionMessage;
import net.l3mon.LogisticsL3mon.company.dto.CompanyDTO;
import net.l3mon.LogisticsL3mon.company.entity.CompanyInviteLink;
import net.l3mon.LogisticsL3mon.company.service.CompanyService;
import net.l3mon.LogisticsL3mon.room.dto.RoomDTO;
import net.l3mon.LogisticsL3mon.room.entity.Room;
import net.l3mon.LogisticsL3mon.room.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    @RequestMapping(path = "/{companyId}/list", method = RequestMethod.GET)
    public ResponseEntity<?> getRoomsListByCompanyId(@PathVariable Long companyId) {
        List<Room> rooms;
        try {
            rooms = roomService.getRoomsListByCompanyId(companyId);
        } catch (GlobalExceptionMessage ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.ok(rooms);
    }

    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public ResponseEntity<?>  addNewRoom(@RequestBody RoomDTO room){
        try {
            return ResponseEntity.ok(roomService.create(room));
        } catch (GlobalExceptionMessage ex) {
            ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
