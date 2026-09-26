package com.nexuscomply.report.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

/** BUG-003: MongoDB document for Reports. */
@Document(collection = "reports")
public class Report {

    @Id
    private String id;

    private String name;
    private String status;       // QUEUED, GENERATING, READY, FAILED
    private String downloadUrl;
    private Instant createdAt;
    private Instant updatedAt;

    public Report() {}

    public String getId()               { return id; }
    public void setId(String id)        { this.id = id; }

    public String getName()             { return name; }
    public void setName(String name)    { this.name = name; }

    public String getStatus()               { return status; }
    public void setStatus(String status)    { this.status = status; }

    public String getDownloadUrl()                  { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl)  { this.downloadUrl = downloadUrl; }

    public Instant getCreatedAt()                 { return createdAt; }
    public void setCreatedAt(Instant createdAt)   { this.createdAt = createdAt; }

    public Instant getUpdatedAt()                 { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt)   { this.updatedAt = updatedAt; }
}
