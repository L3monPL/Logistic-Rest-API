package net.l3mon.LogisticsL3mon.room.repository;

import net.l3mon.LogisticsL3mon.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByCompanyId(Long companyId);
}
