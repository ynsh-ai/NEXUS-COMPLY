package com.nexuscomply.drift.dto;

import java.util.UUID;
import java.time.Instant;

public class DriftDTO {
    private UUID id;
    private UUID deviceId;
    private String description;
    private String driftStatus;
    private Instant detectedAt;

    public DriftDTO() {}

    public DriftDTO(UUID id, UUID deviceId, String description, String driftStatus, Instant detectedAt) {
        this.id = id;
        this.deviceId = deviceId;
        this.description = description;
        this.driftStatus = driftStatus;
        this.detectedAt = detectedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDriftStatus() {
        return driftStatus;
    }

    public void setDriftStatus(String driftStatus) {
        this.driftStatus = driftStatus;
    }

    public Instant getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(Instant detectedAt) {
        this.detectedAt = detectedAt;
    }
}
