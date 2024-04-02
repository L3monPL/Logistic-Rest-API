package net.l3mon.LogisticsL3mon.company.repository;

import net.l3mon.LogisticsL3mon.company.entity.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long> {
    List<CompanyUser> findByUserId(Long userId);
    Optional<CompanyUser> findByUserIdAndCompanyId(Long userId, Long companyId);
    @Query("SELECT cu FROM CompanyUser cu WHERE cu.companyId = :companyId")
    List<CompanyUser> findAllByCompanyId(Long companyId);
}
