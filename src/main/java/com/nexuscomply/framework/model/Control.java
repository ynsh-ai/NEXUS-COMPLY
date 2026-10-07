package com.nexuscomply.framework.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "controls")
public class Control {
    @Id
    private String id;
    private String frameworkId;
    private String controlCode;
    private String title;
    private String description;
    private String severity;
    private String category;
    private String remediationGuidance;
    private List<String> ruleIds = new ArrayList<>();

    public Control() {}

    public Control(String id, String frameworkId, String controlCode, String title, String description, String severity, String category, String remediationGuidance, List<String> ruleIds) {
        this.id = id;
        this.frameworkId = frameworkId;
        this.controlCode = controlCode;
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.category = category;
        this.remediationGuidance = remediationGuidance;
        this.ruleIds = ruleIds != null ? ruleIds : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrameworkId() {
        return frameworkId;
    }

    public void setFrameworkId(String frameworkId) {
        this.frameworkId = frameworkId;
    }

    public String getControlCode() {
        return controlCode;
    }

    public void setControlCode(String controlCode) {
        this.controlCode = controlCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRemediationGuidance() {
        return remediationGuidance;
    }

    public void setRemediationGuidance(String remediationGuidance) {
        this.remediationGuidance = remediationGuidance;
    }

    public List<String> getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(List<String> ruleIds) {
        this.ruleIds = ruleIds != null ? ruleIds : new ArrayList<>();
    }
}
