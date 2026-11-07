package com.cinemas.repository;

import com.cinemas.model.Report;
import com.cinemas.enums.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Page<Report> findByStatus(ReportStatus status, Pageable pageable);

    Page<Report> findByUser_Id(UUID userId, Pageable pageable);
}


