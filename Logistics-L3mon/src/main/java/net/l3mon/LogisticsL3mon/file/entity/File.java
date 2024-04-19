package net.l3mon.LogisticsL3mon.file.entity;

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
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(generator = "files_seq", strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "files_seq", sequenceName = "files_seq", allocationSize = 1)
    private Long id;

    @Column(name = "file_name")
    private String filename;

    @Column(name = "size")
    private Long size;

    @Column(name = "type")
    private String type;

    @Column(name = "data", nullable = false)
    private byte[] data;

    @Column(name = "created_at")
    private String createdAt;

}
