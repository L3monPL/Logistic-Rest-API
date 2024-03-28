package net.l3mon.LogisticsL3mon.room.entity;

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
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(generator = "rooms_id_seq", strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "rooms_id_seq", sequenceName = "rooms_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "name")
    private String name;

    @Column(name = "permission_for_all")
    private boolean permissionForAll;

    @Column(name = "created_at")
    private String createdAt;
}
