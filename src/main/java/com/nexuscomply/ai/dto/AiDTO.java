package com.nexuscomply.ai.dto;

import java.util.UUID;

public class AiDTO {
    private UUID id;
    private String status;
    private String result;

    public AiDTO() {}

    public AiDTO(UUID id, String status, String result) {
        this.id = id;
        this.status = status;
        this.result = result;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
