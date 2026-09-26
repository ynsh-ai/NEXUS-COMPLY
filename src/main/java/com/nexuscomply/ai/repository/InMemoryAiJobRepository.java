package com.nexuscomply.ai.repository;

import com.nexuscomply.ai.model.AiJob;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAiJobRepository implements AiJobRepository {

    private final Map<String, AiJob> store = new ConcurrentHashMap<>();

    @Override
    public AiJob save(AiJob job) {
        if (job.getId() == null) {
            job.setId(UUID.randomUUID().toString());
        }
        if (job.getCreatedAt() == null) {
            job.setCreatedAt(Instant.now());
        }
        job.setUpdatedAt(Instant.now());
        store.put(job.getId(), job);
        return job;
    }

    @Override
    public Optional<AiJob> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public long count() {
        return store.size();
    }
}
