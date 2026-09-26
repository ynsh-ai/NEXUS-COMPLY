package com.nexuscomply.risk.repository;

import com.nexuscomply.risk.model.RiskAssessment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryRiskAssessmentRepository implements RiskAssessmentRepository {

    private final Map<String, RiskAssessment> store = new ConcurrentHashMap<>();

    public InMemoryRiskAssessmentRepository() {
        // Seed default sample risk assessment for verification
        RiskAssessment r1 = new RiskAssessment();
        r1.setId(UUID.randomUUID().toString());
        r1.setTitle("Telnet Enabled Risk");
        r1.setSeverity("HIGH");
        r1.setScore(7.5);
        r1.setDeviceId("dev-001");
        r1.setFindingId("find-001");
        r1.setAuditId("audit-001");
        r1.setCalculatedAt(Instant.now());
        store.put(r1.getId(), r1);
    }

    @Override
    public Page<RiskAssessment> findAll(Pageable pageable) {
        List<RiskAssessment> list = new ArrayList<>(store.values());
        return toPage(list, pageable);
    }

    @Override
    public Page<RiskAssessment> findAllByDeviceIdNotNull(Pageable pageable) {
        List<RiskAssessment> list = store.values().stream()
                .filter(r -> r.getDeviceId() != null)
                .collect(Collectors.toList());
        return toPage(list, pageable);
    }

    @Override
    public Page<RiskAssessment> findAllByFindingIdNotNull(Pageable pageable) {
        List<RiskAssessment> list = store.values().stream()
                .filter(r -> r.getFindingId() != null)
                .collect(Collectors.toList());
        return toPage(list, pageable);
    }

    @Override
    public Optional<RiskAssessment> findFirstByDeviceId(String deviceId) {
        return store.values().stream()
                .filter(r -> deviceId.equalsIgnoreCase(r.getDeviceId()))
                .findFirst();
    }

    @Override
    public Optional<RiskAssessment> findFirstByFindingId(String findingId) {
        return store.values().stream()
                .filter(r -> findingId.equalsIgnoreCase(r.getFindingId()))
                .findFirst();
    }

    @Override
    public List<RiskAssessment> findAllByOrderByCalculatedAtDesc(Pageable pageable) {
        return store.values().stream()
                .sorted(Comparator.comparing(RiskAssessment::getCalculatedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @Override
    public Page<RiskAssessment> findAllByAuditId(String auditId, Pageable pageable) {
        List<RiskAssessment> list = store.values().stream()
                .filter(r -> auditId.equalsIgnoreCase(r.getAuditId()))
                .collect(Collectors.toList());
        return toPage(list, pageable);
    }

    @Override
    public RiskAssessment save(RiskAssessment assessment) {
        if (assessment.getId() == null) {
            assessment.setId(UUID.randomUUID().toString());
        }
        if (assessment.getCalculatedAt() == null) {
            assessment.setCalculatedAt(Instant.now());
        }
        store.put(assessment.getId(), assessment);
        return assessment;
    }

    @Override
    public Optional<RiskAssessment> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public long count() {
        return store.size();
    }

    private Page<RiskAssessment> toPage(List<RiskAssessment> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<RiskAssessment> sublist = (start <= list.size()) ? list.subList(start, end) : Collections.emptyList();
        return new PageImpl<>(sublist, pageable, list.size());
    }
}
