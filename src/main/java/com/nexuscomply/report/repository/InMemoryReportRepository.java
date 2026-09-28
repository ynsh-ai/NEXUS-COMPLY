package com.nexuscomply.report.repository;

import com.nexuscomply.report.model.Report;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryReportRepository implements ReportRepository {

    private final Map<String, Report> store = new ConcurrentHashMap<>();

    public InMemoryReportRepository() {
        Report r1 = new Report();
        r1.setId(UUID.randomUUID().toString());
        r1.setName("Executive Compliance Audit Report");
        r1.setStatus("READY");
        r1.setDownloadUrl("/api/v1/reports/" + r1.getId() + "/download");
        r1.setCreatedAt(Instant.now());
        r1.setUpdatedAt(Instant.now());
        store.put(r1.getId(), r1);
    }

    @Override
    public Report save(Report report) {
        if (report.getId() == null) {
            report.setId(UUID.randomUUID().toString());
        }
        if (report.getCreatedAt() == null) {
            report.setCreatedAt(Instant.now());
        }
        report.setUpdatedAt(Instant.now());
        store.put(report.getId(), report);
        return report;
    }

    @Override
    public Page<Report> findAll(Pageable pageable) {
        List<Report> list = new ArrayList<>(store.values());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Report> sublist = (start <= list.size()) ? list.subList(start, end) : Collections.emptyList();
        return new PageImpl<>(sublist, pageable, list.size());
    }

    @Override
    public Optional<Report> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public long count() {
        return store.size();
    }
}
