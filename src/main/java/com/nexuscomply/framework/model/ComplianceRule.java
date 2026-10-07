package com.nexuscomply.framework.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "compliance_rules")
public class ComplianceRule {
    @Id
    @JsonProperty("_id")
    @JsonAlias({"id", "_id"})
    private String id;

    @JsonAlias({"controlId", "officialControlId", "official_control_id"})
    private String controlId;

    @JsonAlias({"ruleName", "ruleIntent", "rule_intent"})
    private String ruleName;

    private String description;

    @JsonAlias({"targetPath", "canonicalField", "canonical_field"})
    private String targetPath;

    private String operator;
    private Object expectedValue;
    private String severity;
    private String status = "ACTIVE";
    private String sourceType = "DERIVED_TEMPLATE";
    private String notes;

    public ComplianceRule() {}

    public ComplianceRule(String id, String controlId, String ruleName, String description, String targetPath, String operator, Object expectedValue, String severity) {
        this.id = id;
        this.controlId = controlId;
        this.ruleName = ruleName;
        this.description = description;
        this.targetPath = targetPath;
        this.operator = operator;
        this.expectedValue = expectedValue;
        this.severity = severity;
        this.status = "ACTIVE";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getControlId() {
        return controlId;
    }

    public void setControlId(String controlId) {
        this.controlId = controlId;
    }

    public String getOfficialControlId() {
        return controlId;
    }

    public void setOfficialControlId(String officialControlId) {
        this.controlId = officialControlId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleIntent() {
        return ruleName;
    }

    public void setRuleIntent(String ruleIntent) {
        this.ruleName = ruleIntent;
    }

    public String getDescription() {
        return description != null ? description : ruleName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getCanonicalField() {
        return targetPath;
    }

    public void setCanonicalField(String canonicalField) {
        this.targetPath = canonicalField;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(Object expectedValue) {
        this.expectedValue = expectedValue;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
