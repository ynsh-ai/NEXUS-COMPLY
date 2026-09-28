package com.nexuscomply.audit.service;

import com.nexuscomply.audit.dto.AuditReportResponse;
import com.nexuscomply.audit.dto.AuditStatusResponse;
import com.nexuscomply.audit.dto.CreateAuditRequest;
import com.nexuscomply.audit.model.*;
import com.nexuscomply.common.exception.ApiException;
import com.nexuscomply.common.exception.ResourceNotFoundException;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuditServiceImpl implements AuditService {

    private final Map<String, Audit> auditStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void initSampleAudit() {
        Audit audit = new Audit();
        audit.setId("audit-001");
        audit.setAuditNumber("AUD-2026-0001");
        audit.setName("Core Infrastructure Security Baseline Audit");
        audit.setType("COMPREHENSIVE");
        audit.setStatus("COMPLETED");

        AuditScope scope = new AuditScope();
        scope.setDeviceIds(List.of("dev-cisco-core-01"));
        scope.setConfigurationIds(List.of("cfg-001"));
        scope.setFrameworkIds(List.of("cis-cisco-ios-xe", "nist-sp-800-53-r5"));
        audit.setScope(scope);

        AuditSummary summary = new AuditSummary();
        summary.setComplianceScore(92.5);
        summary.setTotalControls(45);
        summary.setPassedControls(41);
        summary.setFailedControls(3);
        summary.setUnknownControls(1);
        summary.setTotalFindings(3);
        summary.setCriticalFindings(0);
        summary.setHighFindings(1);
        summary.setMediumFindings(2);
        summary.setLowFindings(0);
        audit.setSummary(summary);

        AuditRisk risk = new AuditRisk();
        risk.setOverallRiskScore(28.5);
        risk.setRiskLevel("LOW");
        risk.setCriticalCount(0);
        risk.setHighCount(1);
        risk.setMediumCount(2);
        risk.setLowCount(0);
        audit.setRisk(risk);

        List<FrameworkResult> frameworkResults = new ArrayList<>();
        frameworkResults.add(new FrameworkResult("cis-cisco-ios-xe", "CIS Cisco IOS XE Benchmark", 92.5, 41, 3, 1));
        frameworkResults.add(new FrameworkResult("nist-sp-800-53-r5", "NIST SP 800-53 Rev 5", 94.0, 30, 2, 0));
        audit.setFrameworkResults(frameworkResults);

        audit.setFindingIds(List.of("find-001", "find-002", "find-003"));
        audit.setStartedAt(Instant.now().minusSeconds(3600));
        audit.setCompletedAt(Instant.now().minusSeconds(3500));

        auditStore.put(audit.getId(), audit);
    }

    @Override
    public Audit createAudit(CreateAuditRequest request) {
        if (request == null || request.getName() == null || request.getName().isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "Audit name is required.");
        }

        String id = "audit-" + UUID.randomUUID().toString().substring(0, 8);
        Audit audit = new Audit();
        audit.setId(id);
        audit.setAuditNumber("AUD-2026-" + String.format("%04d", auditStore.size() + 1));
        audit.setName(request.getName());
        audit.setType(request.getType() != null ? request.getType() : "COMPREHENSIVE");
        audit.setStatus("COMPLETED");

        AuditScope scope = new AuditScope();
        scope.setDeviceIds(request.getDeviceIds() != null ? request.getDeviceIds() : List.of("dev-cisco-core-01"));
        scope.setConfigurationIds(request.getConfigurationIds() != null ? request.getConfigurationIds() : List.of("cfg-001"));
        scope.setFrameworkIds(request.getFrameworkIds() != null ? request.getFrameworkIds() : List.of("cis-cisco-ios-xe"));
        audit.setScope(scope);

        AuditSummary summary = new AuditSummary();
        summary.setComplianceScore(95.0);
        summary.setTotalControls(20);
        summary.setPassedControls(19);
        summary.setFailedControls(1);
        summary.setUnknownControls(0);
        summary.setTotalFindings(1);
        summary.setHighFindings(1);
        audit.setSummary(summary);

        AuditRisk risk = new AuditRisk();
        risk.setOverallRiskScore(22.0);
        risk.setRiskLevel("LOW");
        risk.setHighCount(1);
        audit.setRisk(risk);

        List<FrameworkResult> results = new ArrayList<>();
        results.add(new FrameworkResult("cis-cisco-ios-xe", "CIS Cisco IOS XE Benchmark", 95.0, 19, 1, 0));
        audit.setFrameworkResults(results);

        audit.setFindingIds(List.of("find-001"));
        audit.setStartedAt(Instant.now());
        audit.setCompletedAt(Instant.now());

        auditStore.put(id, audit);
        return audit;
    }

    @Override
    public List<Audit> getAllAudits() {
        return new ArrayList<>(auditStore.values());
    }

    @Override
    public Audit getAuditById(String id) {
        Audit a = auditStore.get(id);
        if (a == null) {
            throw new ResourceNotFoundException("Audit", id);
        }
        return a;
    }

    @Override
    public AuditStatusResponse getAuditStatus(String id) {
        Audit a = getAuditById(id);
        int progress = "COMPLETED".equals(a.getStatus()) ? 100 : 50;
        return new AuditStatusResponse(a.getId(), a.getStatus(), progress, "Audit evaluation is " + a.getStatus().toLowerCase());
    }

    @Override
    public Audit cancelAudit(String id) {
        Audit a = getAuditById(id);
        a.setStatus("CANCELLED");
        return a;
    }

    @Override
    public Audit rerunAudit(String id) {
        Audit a = getAuditById(id);
        a.setStatus("COMPLETED");
        a.setStartedAt(Instant.now());
        a.setCompletedAt(Instant.now());
        return a;
    }

    @Override
    public AuditSummary getAuditSummary(String id) {
        return getAuditById(id).getSummary();
    }

    @Override
    public List<FrameworkResult> getFrameworkResults(String id) {
        return getAuditById(id).getFrameworkResults();
    }

    @Override
    public List<String> getAuditFindings(String id) {
        return getAuditById(id).getFindingIds();
    }

    @Override
    public AuditRisk getAuditRisk(String id) {
        return getAuditById(id).getRisk();
    }

    @Override
    public AuditReportResponse generateReport(String id) {
        Audit a = getAuditById(id);
        String reportId = "rep-" + UUID.randomUUID().toString().substring(0, 8);
        return new AuditReportResponse(
                reportId,
                a.getId(),
                "Compliance Audit Report - " + a.getName(),
                "PDF",
                "GENERATED",
                "/api/v1/reports/" + reportId + "/download",
                Instant.now()
        );
    }
}
