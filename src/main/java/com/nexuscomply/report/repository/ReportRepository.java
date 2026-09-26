package com.nexuscomply.report.repository;

import com.nexuscomply.report.model.Report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReportRepository {
    Report save(Report report);
    Page<Report> findAll(Pageable pageable);
    Optional<Report> findById(String id);
    long count();
}
