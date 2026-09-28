package com.nexuscomply.drift.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.Instant;

/** BUG-003: MongoDB document for Drift Events collection. */
@Document(collection = "drift_events")
public class DriftEvent {

    @Id
    private String id;

    @Indexed
    private String deviceId;

    private String description;
    private String driftStatus;  // DETECTED, ACKNOWLEDGED, RESOLVED
    private Instant detectedAt;

    /** IDs of affected control rules (non-cyber reference, stored as strings). */
    private java.util.List<String> affectedControlIds;

    public DriftEvent() {}

    public String getId()               { return id; }
    public void setId(String id)        { this.id = id; }

    public String getDeviceId()               { return deviceId; }
    public void setDeviceId(String deviceId)  { this.deviceId = deviceId; }

    public String getDescription()                { return description; }
    public void setDescription(String description){ this.description = description; }

    public String getDriftStatus()                  { return driftStatus; }
    public void setDriftStatus(String driftStatus)  { this.driftStatus = driftStatus; }

    public Instant getDetectedAt()                  { return detectedAt; }
    public void setDetectedAt(Instant detectedAt)   { this.detectedAt = detectedAt; }

    public java.util.List<String> getAffectedControlIds()                                 { return affectedControlIds; }
    public void setAffectedControlIds(java.util.List<String> affectedControlIds)          { this.affectedControlIds = affectedControlIds; }
}
