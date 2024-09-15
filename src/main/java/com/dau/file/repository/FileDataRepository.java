package com.dau.file.repository;

import com.dau.file.entity.FileData;
import com.dau.file.entity.enums.STATUS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Long> {

    Optional<FileData> findByStandardTimeAndStatus(LocalDateTime standardTime, STATUS status);

}
