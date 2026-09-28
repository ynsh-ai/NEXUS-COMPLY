package com.nexuscomply.framework.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Framework {
    private String id;
    private String code;
    private String name;
    private String version;
    private String type;
    private String description;
    private Integer totalControls;
    private List<String> supportedVendors;

    public Framework() {}

    public Framework(String id, String code, String name, String version, String type, String description, Integer totalControls, List<String> supportedVendors) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.version = version;
        this.type = type;
        this.description = description;
        this.totalControls = totalControls;
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
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalControls() {
        return totalControls;
    }

    public void setTotalControls(Integer totalControls) {
        this.totalControls = totalControls;
    }

    public List<String> getSupportedVendors() {
        return supportedVendors;
    }

    public void setSupportedVendors(List<String> supportedVendors) {
        this.supportedVendors = supportedVendors;
    }
}
