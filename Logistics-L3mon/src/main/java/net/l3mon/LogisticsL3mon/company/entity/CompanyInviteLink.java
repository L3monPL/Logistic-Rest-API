package net.l3mon.LogisticsL3mon.company.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company_invite_link")
public class CompanyInviteLink {
    @Id
    @GeneratedValue(generator = "company_invite_links_id_seq", strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "company_invite_links_id_seq", sequenceName = "company_invite_links_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "code")
    private String code;

    @Column(name = "requires_acceptance")
    private boolean requiresAcceptance;

    @Column(name = "created_at")
    private String createdAt;
}
