package com.nexuscomply.framework.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "frameworks")
public class Framework {
    @Id
    private String id;
    private String code;
    private String name;
    private String version;
    @JsonProperty("version_scope")
    @JsonAlias({"versionScope", "version_scope"})
    private String versionScope;
    private String type;
    private String category;
    private String description;
    private String note;
    private Integer totalControls;
    private Integer controls;
    private Integer mappedRules = 0;
    private Integer coverage = 0;
    private List<String> supportedVendors;

    public Framework() {}

    public Framework(String id, String code, String name, String version, String type, String description, Integer totalControls, List<String> supportedVendors) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.version = version;
        this.type = type;
        this.category = type;
        this.description = description;
        this.note = description;
        this.totalControls = totalControls;
        this.controls = totalControls;
        this.supportedVendors = supportedVendors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version != null ? version : versionScope;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionScope() {
        return versionScope != null ? versionScope : version;
    }

    public void setVersionScope(String versionScope) {
        this.versionScope = versionScope;
    }

    public String getType() {
        return type != null ? type : category;
    }

    public void setType(String type) {
        this.type = type;
        if (this.category == null) this.category = type;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category != null ? category : type;
    }

    public void setCategory(String category) {
        this.category = category;
        if (this.type == null) this.type = category;
    }

    public String getDescription() {
        return description != null ? description : note;
    }

    public void setDescription(String description) {
        this.description = description;
        if (this.note == null) this.note = description;
    }

    @JsonProperty("note")
    public String getNote() {
        return note != null ? note : description;
    }

    public void setNote(String note) {
        this.note = note;
        if (this.description == null) this.description = note;
    }

    public Integer getTotalControls() {
        return totalControls != null ? totalControls : controls;
    }

    public void setTotalControls(Integer totalControls) {
        this.totalControls = totalControls;
        if (this.controls == null) this.controls = totalControls;
    }

    @JsonProperty("controls")
    public Integer getControls() {
        return controls != null ? controls : totalControls;
    }

    public void setControls(Integer controls) {
        this.controls = controls;
        if (this.totalControls == null) this.totalControls = controls;
    }

    public Integer getMappedRules() {
        return mappedRules != null ? mappedRules : 0;
    }

    public void setMappedRules(Integer mappedRules) {
        this.mappedRules = mappedRules;
    }

    public Integer getCoverage() {
        return coverage != null ? coverage : 0;
    }

    public void setCoverage(Integer coverage) {
        this.coverage = coverage;
    }

    public List<String> getSupportedVendors() {
        return supportedVendors;
    }

    public void setSupportedVendors(List<String> supportedVendors) {
        this.supportedVendors = supportedVendors;
    }
}
