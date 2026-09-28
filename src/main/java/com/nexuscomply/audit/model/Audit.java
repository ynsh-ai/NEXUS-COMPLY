package com.nexuscomply.audit.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Audit {
    private String id;
    private String auditNumber;
    private String name;
    private String type = "COMPREHENSIVE";
    private String status = "COMPLETED"; // QUEUED, RUNNING, COMPLETED, FAILED, CANCELLED
    private AuditScope scope = new AuditScope();
    private AuditSummary summary = new AuditSummary();
    private AuditRisk risk = new AuditRisk();
    private List<FrameworkResult> frameworkResults = new ArrayList<>();
    private List<String> findingIds = new ArrayList<>();
    private Instant startedAt = Instant.now();
    private Instant completedAt;

    public Audit() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuditNumber() {
        return auditNumber;
    }

    public void setAuditNumber(String auditNumber) {
        this.auditNumber = auditNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AuditScope getScope() {
        return scope;
    }

    public void setScope(AuditScope scope) {
        this.scope = scope != null ? scope : new AuditScope();
    }

    public AuditSummary getSummary() {
        return summary;
    }

    public void setSummary(AuditSummary summary) {
        this.summary = summary != null ? summary : new AuditSummary();
    }

    public AuditRisk getRisk() {
        return risk;
    }

    public void setRisk(AuditRisk risk) {
        this.risk = risk != null ? risk : new AuditRisk();
    }

    public List<FrameworkResult> getFrameworkResults() {
        return frameworkResults;
    }

    public void setFrameworkResults(List<FrameworkResult> frameworkResults) {
        this.frameworkResults = frameworkResults != null ? frameworkResults : new ArrayList<>();
    }

    public List<String> getFindingIds() {
        return findingIds;
    }

    public void setFindingIds(List<String> findingIds) {
        this.findingIds = findingIds != null ? findingIds : new ArrayList<>();
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Instant completedAt) {
        this.completedAt = completedAt;
    }
}
