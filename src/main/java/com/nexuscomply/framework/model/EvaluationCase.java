package com.nexuscomply.framework.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "evaluation_cases")
public class EvaluationCase {
    @Id
    @JsonProperty("_id")
    @JsonAlias({"id", "_id"})
    private String id;

    private String vendor;
    private String platform;

    @JsonProperty("caseClass")
    @JsonAlias({"caseClass", "case_class"})
    private String caseClass;

    private String summary;

    @JsonProperty("expectedResult")
    @JsonAlias({"expectedResult", "expected_result"})
    private String expectedResult;

    private String status = "SEED";

    public EvaluationCase() {}

    public EvaluationCase(String id, String vendor, String platform, String caseClass, String summary, String expectedResult, String status) {
        this.id = id;
        this.vendor = vendor;
        this.platform = platform;
        this.caseClass = caseClass;
        this.summary = summary;
        this.expectedResult = expectedResult;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCaseClass() {
        return caseClass;
    }

    public void setCaseClass(String caseClass) {
        this.caseClass = caseClass;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
