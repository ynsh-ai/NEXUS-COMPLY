package com.nexuscomply.framework.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComplianceRule {
    private String id;
    private String controlId;
    private String ruleName;
    private String description;
    private String targetPath;
    private String operator;
    private Object expectedValue;
    private String severity;

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

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getDescription() {
        return description;
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
}
