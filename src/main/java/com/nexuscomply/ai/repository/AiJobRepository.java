package com.nexuscomply.ai.repository;

import com.nexuscomply.ai.model.AiJob;


import java.util.Optional;

public interface AiJobRepository {
    AiJob save(AiJob job);
    Optional<AiJob> findById(String id);
    long count();
}
