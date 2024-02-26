package net.l3mon.LogisticsL3mon.repository;

import net.l3mon.LogisticsL3mon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
