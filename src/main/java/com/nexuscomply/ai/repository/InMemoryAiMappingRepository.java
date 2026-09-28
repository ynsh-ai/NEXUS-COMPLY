package com.nexuscomply.ai.repository;

import com.nexuscomply.ai.model.AiMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAiMappingRepository implements AiMappingRepository {

    private final Map<String, AiMapping> store = new ConcurrentHashMap<>();

    public InMemoryAiMappingRepository() {
        AiMapping m1 = new AiMapping();
        m1.setId(UUID.randomUUID().toString());
        m1.setStatus("PENDING_REVIEW");
        m1.setResult("PASS");
        m1.setCreatedAt(Instant.now());
        m1.setUpdatedAt(Instant.now());
        store.put(m1.getId(), m1);
    }

    @Override
    public Page<AiMapping> findAll(Pageable pageable) {
        List<AiMapping> list = new ArrayList<>(store.values());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<AiMapping> sublist = (start <= list.size()) ? list.subList(start, end) : Collections.emptyList();
        return new PageImpl<>(sublist, pageable, list.size());
    }

    @Override
    public Optional<AiMapping> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public AiMapping save(AiMapping mapping) {
        if (mapping.getId() == null) {
            mapping.setId(UUID.randomUUID().toString());
        }
        if (mapping.getCreatedAt() == null) {
            mapping.setCreatedAt(Instant.now());
        }
        mapping.setUpdatedAt(Instant.now());
        store.put(mapping.getId(), mapping);
        return mapping;
    }

    @Override
    public long count() {
        return store.size();
    }
}
