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
@Table(name = "company_user_waiting_to_join")
public class CompanyUserWaitingToJoin {
    @Id
    @GeneratedValue(generator = "company_user_waiting_to_joins_seq", strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "company_user_waiting_to_joins_seq", sequenceName = "company_user_waiting_to_joins_seq", allocationSize = 1)
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at")
    private String createdAt;
}
