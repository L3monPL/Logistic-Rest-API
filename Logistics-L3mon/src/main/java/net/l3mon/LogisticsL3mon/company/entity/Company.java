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
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(generator = "companys_id_seq", strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "companys_id_seq", sequenceName = "companys_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "shortname")
    private String shortName;
    @Column(name = "isenable")
    private boolean isEnable;
    @Column(name = "createdat")
    private String createdAt;
}
