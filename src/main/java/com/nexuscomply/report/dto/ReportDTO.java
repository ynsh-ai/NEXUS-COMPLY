package com.nexuscomply.report.dto;

import java.util.UUID;
import java.time.Instant;

public class ReportDTO {
    private UUID id;
    private String name;
    private String status;
    private String downloadUrl;
    private Instant createdAt;

    public ReportDTO() {}

    public ReportDTO(UUID id, String name, String status, String downloadUrl, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.downloadUrl = downloadUrl;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
