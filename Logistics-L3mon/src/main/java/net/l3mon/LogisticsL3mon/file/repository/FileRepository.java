package net.l3mon.LogisticsL3mon.file.repository;

import net.l3mon.LogisticsL3mon.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

}
