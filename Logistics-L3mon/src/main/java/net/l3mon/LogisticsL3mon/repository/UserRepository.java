package net.l3mon.LogisticsL3mon.repository;

import net.l3mon.LogisticsL3mon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String username);

    Optional<User> findUserByEmail(String email);
}
