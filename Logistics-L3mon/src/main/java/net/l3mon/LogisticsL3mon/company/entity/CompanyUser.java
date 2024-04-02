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
@Table(name = "company_user")
public class CompanyUser {
    @Id
    @GeneratedValue(generator = "company_users", strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "company_users", sequenceName = "company_users", allocationSize = 1)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "role")
    private String role;

    @Column(name = "created_at")
    private String createdAt;

}
