package net.l3mon.LogisticsL3mon.company.repository;

import net.l3mon.LogisticsL3mon.company.entity.CompanyUserWaitingToJoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyUserWaitingToJoinRepository extends JpaRepository<CompanyUserWaitingToJoin, Long> {
    Optional<CompanyUserWaitingToJoin> findByUserIdAndCompanyId(Long id, Long companyId);

    List<CompanyUserWaitingToJoin> findAllByCompanyId(Long companyId);
}
