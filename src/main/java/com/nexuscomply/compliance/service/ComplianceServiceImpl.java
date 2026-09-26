package com.nexuscomply.compliance.service;

import com.nexuscomply.common.exception.ResourceNotFoundException;
import com.nexuscomply.compliance.dto.ComplianceSummaryResponse;
import com.nexuscomply.compliance.dto.EvaluateComplianceRequest;
import com.nexuscomply.compliance.model.ComplianceResult;
import com.nexuscomply.compliance.model.UnknownComplianceItem;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ComplianceServiceImpl implements ComplianceService {

    private final Map<String, List<ComplianceResult>> resultsByAudit = new ConcurrentHashMap<>();
    private final Map<String, List<UnknownComplianceItem>> unknownsByAudit = new ConcurrentHashMap<>();

    @PostConstruct
    public void initSampleResults() {
        String auditId = "audit-001";
        List<ComplianceResult> results = new ArrayList<>();
        results.add(new ComplianceResult("res-001", auditId, "cis-1-1-1", "cis-cisco-ios-xe", "PASSED", null, "SSH is enabled and configured on VTY lines"));
        results.add(new ComplianceResult("res-002", auditId, "cis-1-1-2", "cis-cisco-ios-xe", "PASSED", null, "AAA new-model is enabled"));
        results.add(new ComplianceResult("res-003", auditId, "cis-1-1-3", "cis-cisco-ios-xe", "FAILED", "find-001", "Telnet service is enabled on auxiliary port"));
        results.add(new ComplianceResult("res-004", auditId, "nist-ac-17", "nist-sp-800-53-r5", "PASSED", null, "Remote access is secured with cryptographic algorithms"));
        resultsByAudit.put(auditId, results);

        List<UnknownComplianceItem> unknowns = new ArrayList<>();
        unknowns.add(new UnknownComplianceItem("cis-2-1-4", "rule-ntp-01", "NTP authentication status could not be derived from snippet", "security.ntp.auth"));
        unknownsByAudit.put(auditId, unknowns);
    }

    @Override
    public List<ComplianceResult> evaluateCompliance(EvaluateComplianceRequest request) {
        String auditId = "audit-" + UUID.randomUUID().toString().substring(0, 8);
        List<ComplianceResult> results = new ArrayList<>();
        results.add(new ComplianceResult("res-" + UUID.randomUUID().toString().substring(0, 8), auditId, "cis-1-1-1", "cis-cisco-ios-xe", "PASSED", null, "SSH is properly enabled"));
        results.add(new ComplianceResult("res-" + UUID.randomUUID().toString().substring(0, 8), auditId, "cis-1-1-2", "cis-cisco-ios-xe", "PASSED", null, "AAA subsystem active"));
        results.add(new ComplianceResult("res-" + UUID.randomUUID().toString().substring(0, 8), auditId, "cis-1-1-3", "cis-cisco-ios-xe", "FAILED", "find-001", "Insecure service found"));

        resultsByAudit.put(auditId, results);
        unknownsByAudit.put(auditId, List.of());
        return results;
    }

    @Override
    public ComplianceResult evaluateControl(String controlId, EvaluateComplianceRequest request) {
        String auditId = "eval-" + UUID.randomUUID().toString().substring(0, 8);
        ComplianceResult res = new ComplianceResult(
                "res-" + UUID.randomUUID().toString().substring(0, 8),
                auditId,
                controlId,
                "cis-cisco-ios-xe",
                "PASSED",
                null,
                "Control " + controlId + " evaluated successfully."
        );
        resultsByAudit.computeIfAbsent(auditId, k -> new ArrayList<>()).add(res);
        return res;
    }

    @Override
    public List<ComplianceResult> evaluateFramework(String frameworkId, EvaluateComplianceRequest request) {
        String auditId = "eval-fw-" + UUID.randomUUID().toString().substring(0, 8);
        List<ComplianceResult> results = List.of(
                new ComplianceResult("res-" + UUID.randomUUID().toString().substring(0, 8), auditId, "cis-1-1-1", frameworkId, "PASSED", null, "Rule passed"),
                new ComplianceResult("res-" + UUID.randomUUID().toString().substring(0, 8), auditId, "cis-1-1-2", frameworkId, "PASSED", null, "Rule passed")
        );
        resultsByAudit.put(auditId, new ArrayList<>(results));
        return results;
    }

    @Override
    public List<ComplianceResult> getResultsByAudit(String auditId) {
        List<ComplianceResult> res = resultsByAudit.get(auditId);
        if (res == null) {
            throw new ResourceNotFoundException("Compliance results for audit", auditId);
        }
        return res;
    }

    @Override
    public ComplianceSummaryResponse getSummaryByAudit(String auditId) {
        List<ComplianceResult> res = getResultsByAudit(auditId);
        int passed = (int) res.stream().filter(r -> "PASSED".equalsIgnoreCase(r.getStatus())).count();
        int failed = (int) res.stream().filter(r -> "FAILED".equalsIgnoreCase(r.getStatus())).count();
        int unknowns = (int) res.stream().filter(r -> "UNKNOWN".equalsIgnoreCase(r.getStatus())).count();
        double score = res.isEmpty() ? 100.0 : ((double) passed / res.size()) * 100.0;
        return new ComplianceSummaryResponse(auditId, score, res.size(), passed, failed, unknowns);
    }

    @Override
    public List<UnknownComplianceItem> getUnknownsByAudit(String auditId) {
        getResultsByAudit(auditId); // check exists
        return unknownsByAudit.getOrDefault(auditId, List.of());
    }
}
