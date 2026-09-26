package com.nexuscomply.finding.service;

import com.nexuscomply.common.exception.ResourceNotFoundException;
import com.nexuscomply.finding.dto.UpdateSeverityRequest;
import com.nexuscomply.finding.dto.UpdateStatusRequest;
import com.nexuscomply.finding.model.Evidence;
import com.nexuscomply.finding.model.Finding;
import com.nexuscomply.finding.model.FindingHistoryEntry;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FindingServiceImpl implements FindingService {

    private final Map<String, Finding> findingStore = new ConcurrentHashMap<>();
    private final Map<String, Evidence> evidenceStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void initSampleFindings() {
        Finding finding = new Finding();
        finding.setId("find-001");
        finding.setAuditId("audit-001");
        finding.setDeviceId("dev-cisco-core-01");
        finding.setConfigurationId("cfg-001");
        finding.setVersionId("v1.0.0");
        finding.setControlId("cis-1-1-3");
        finding.setRuleId("rule-telnet-01");
        finding.setTitle("Telnet service is enabled on auxiliary line");
        finding.setDescription("Unencrypted Telnet management session allows cleartext interception of administrator credentials.");
        finding.setSeverity("HIGH");
        finding.setStatus("OPEN");
        finding.setRemediationGuidance("Remove 'transport input telnet' and configure 'transport input ssh' under line aux 0.");
        finding.setEvidenceIds(List.of("evid-001"));
        finding.setHistory(new ArrayList<>(List.of(
                new FindingHistoryEntry("DETECTED", "SYSTEM", "Automatically detected during audit AUD-2026-0001")
        )));
        finding.setCreatedAt(Instant.now().minusSeconds(7200));
        finding.setUpdatedAt(Instant.now().minusSeconds(7200));
        findingStore.put(finding.getId(), finding);

        Evidence evidence = new Evidence(
                "evid-001",
                finding.getId(),
                "audit-001",
                "dev-cisco-core-01",
                "cfg-001",
                "v1.0.0",
                "line aux 0\n transport input telnet\n",
                45,
                46,
                "/configs/core-router-01.cfg"
        );
        evidenceStore.put(evidence.getId(), evidence);
    }

    @Override
    public List<Finding> filterFindings(String auditId, String severity, String status, String deviceId) {
        return findingStore.values().stream()
                .filter(f -> auditId == null || auditId.equalsIgnoreCase(f.getAuditId()))
                .filter(f -> severity == null || severity.equalsIgnoreCase(f.getSeverity()))
                .filter(f -> status == null || status.equalsIgnoreCase(f.getStatus()))
                .filter(f -> deviceId == null || deviceId.equalsIgnoreCase(f.getDeviceId()))
                .toList();
    }

    @Override
    public Finding getFindingById(String id) {
        Finding f = findingStore.get(id);
        if (f == null) {
            throw new ResourceNotFoundException("Finding", id);
        }
        return f;
    }

    @Override
    public Finding updateFindingStatus(String id, UpdateStatusRequest request) {
        Finding f = getFindingById(id);
        if (request != null && request.getStatus() != null) {
            String oldStatus = f.getStatus();
            f.setStatus(request.getStatus().toUpperCase());
            f.setUpdatedAt(Instant.now());
            String note = request.getNote() != null ? request.getNote() : "Status updated from " + oldStatus + " to " + f.getStatus();
            f.getHistory().add(new FindingHistoryEntry("STATUS_CHANGE", "USER", note));
        }
        return f;
    }

    @Override
    public Finding updateFindingSeverity(String id, UpdateSeverityRequest request) {
        Finding f = getFindingById(id);
        if (request != null && request.getSeverity() != null) {
            String oldSeverity = f.getSeverity();
            f.setSeverity(request.getSeverity().toUpperCase());
            f.setUpdatedAt(Instant.now());
            String note = request.getNote() != null ? request.getNote() : "Severity updated from " + oldSeverity + " to " + f.getSeverity();
            f.getHistory().add(new FindingHistoryEntry("SEVERITY_OVERRIDE", "USER", note));
        }
        return f;
    }

    @Override
    public List<FindingHistoryEntry> getFindingHistory(String id) {
        return getFindingById(id).getHistory();
    }

    @Override
    public List<Finding> getRelatedFindings(String id) {
        Finding f = getFindingById(id);
        return findingStore.values().stream()
                .filter(other -> !other.getId().equals(id))
                .filter(other -> (f.getDeviceId() != null && f.getDeviceId().equals(other.getDeviceId()))
                        || (f.getControlId() != null && f.getControlId().equals(other.getControlId())))
                .toList();
    }

    @Override
    public Finding acknowledgeFinding(String id, String note) {
        Finding f = getFindingById(id);
        f.setStatus("ACKNOWLEDGED");
        f.setUpdatedAt(Instant.now());
        f.getHistory().add(new FindingHistoryEntry("ACKNOWLEDGED", "USER", note != null ? note : "Acknowledged by compliance team"));
        return f;
    }

    @Override
    public Finding resolveFinding(String id, String note) {
        Finding f = getFindingById(id);
        f.setStatus("RESOLVED");
        f.setUpdatedAt(Instant.now());
        f.getHistory().add(new FindingHistoryEntry("RESOLVED", "USER", note != null ? note : "Marked as resolved with verified configuration change"));
        return f;
    }

    @Override
    public List<Evidence> getFindingEvidence(String findingId) {
        getFindingById(findingId); // validate finding exists
        return evidenceStore.values().stream()
                .filter(e -> findingId.equals(e.getFindingId()))
                .toList();
    }

    @Override
    public Evidence getEvidenceById(String id) {
        Evidence e = evidenceStore.get(id);
        if (e == null) {
            throw new ResourceNotFoundException("Evidence", id);
        }
        return e;
    }

    @Override
    public Map<String, Object> getEvidenceSource(String id) {
        Evidence e = getEvidenceById(id);
        Map<String, Object> source = new HashMap<>();
        source.put("evidenceId", e.getId());
        source.put("sourcePath", e.getSourcePath());
        source.put("startLine", e.getStartLine());
        source.put("endLine", e.getEndLine());
        source.put("snippet", e.getSnippet());
        return source;
    }

    @Override
    public Map<String, Object> getEvidenceConfiguration(String id) {
        Evidence e = getEvidenceById(id);
        Map<String, Object> config = new HashMap<>();
        config.put("evidenceId", e.getId());
        config.put("configurationId", e.getConfigurationId());
        config.put("versionId", e.getVersionId());
        config.put("deviceId", e.getDeviceId());
        config.put("snippet", e.getSnippet());
        return config;
    }
}
