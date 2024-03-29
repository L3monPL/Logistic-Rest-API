package net.l3mon.LogisticsL3mon.company.repository;

import net.l3mon.LogisticsL3mon.company.entity.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long> {
}
