package com.nexuscomply.framework.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FrameworkMapping {
    private String id;
    private String sourceFrameworkId;
    private String sourceControlId;
    private String targetFrameworkId;
    private String targetControlId;
    private String relationship;
    private Double confidence;

    public FrameworkMapping() {}

    public FrameworkMapping(String id, String sourceFrameworkId, String sourceControlId, String targetFrameworkId, String targetControlId, String relationship, Double confidence) {
        this.id = id;
        this.sourceFrameworkId = sourceFrameworkId;
        this.sourceControlId = sourceControlId;
        this.targetFrameworkId = targetFrameworkId;
        this.targetControlId = targetControlId;
        this.relationship = relationship;
        this.confidence = confidence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceFrameworkId() {
        return sourceFrameworkId;
    }

    public void setSourceFrameworkId(String sourceFrameworkId) {
        this.sourceFrameworkId = sourceFrameworkId;
    }

    public String getSourceControlId() {
        return sourceControlId;
    }

    public void setSourceControlId(String sourceControlId) {
        this.sourceControlId = sourceControlId;
    }

    public String getTargetFrameworkId() {
        return targetFrameworkId;
    }

    public void setTargetFrameworkId(String targetFrameworkId) {
        this.targetFrameworkId = targetFrameworkId;
    }

    public String getTargetControlId() {
        return targetControlId;
    }

    public void setTargetControlId(String targetControlId) {
        this.targetControlId = targetControlId;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}
