package com.nexuscomply.framework.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "framework_mappings")
public class FrameworkMapping {
    @Id
    private String id;
    private String sourceFrameworkId;
    private String sourceControlId;
    private String targetFrameworkId;
    private String targetControlId;

    @JsonAlias({"relationship", "mappingType", "mapping_type"})
    private String relationship = "DIRECT"; // DIRECT, RELATED, PARTIAL, SUPPORTING_EVIDENCE

    private String mappingType = "DIRECT";
    private Double confidence;

    public FrameworkMapping() {}

    public FrameworkMapping(String id, String sourceFrameworkId, String sourceControlId, String targetFrameworkId, String targetControlId, String relationship, Double confidence) {
        this.id = id;
        this.sourceFrameworkId = sourceFrameworkId;
        this.sourceControlId = sourceControlId;
        this.targetFrameworkId = targetFrameworkId;
        this.targetControlId = targetControlId;
        this.relationship = relationship;
        this.mappingType = relationship;
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
        return relationship != null ? relationship : mappingType;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
        if (this.mappingType == null) {
            this.mappingType = relationship;
        }
    }

    public String getMappingType() {
        return mappingType != null ? mappingType : relationship;
    }

    public void setMappingType(String mappingType) {
        this.mappingType = mappingType;
        if (this.relationship == null) {
            this.relationship = mappingType;
        }
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }
}
