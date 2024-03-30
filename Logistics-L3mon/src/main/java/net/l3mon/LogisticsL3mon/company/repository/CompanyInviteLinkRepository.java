package net.l3mon.LogisticsL3mon.company.repository;

import net.l3mon.LogisticsL3mon.company.entity.CompanyInviteLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyInviteLinkRepository extends JpaRepository<CompanyInviteLink, Long> {
    Optional<CompanyInviteLink> findByCompanyId(Long companyId);

    Optional<CompanyInviteLink> findByCode(String code);
}
