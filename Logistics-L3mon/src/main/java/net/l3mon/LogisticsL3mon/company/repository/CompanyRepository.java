package net.l3mon.LogisticsL3mon.company.repository;

import net.l3mon.LogisticsL3mon.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
