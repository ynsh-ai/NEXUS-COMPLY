package com.nexuscomply.ai.repository;

import com.nexuscomply.ai.model.AiMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AiMappingRepository {
    Page<AiMapping> findAll(Pageable pageable);
    Optional<AiMapping> findById(String id);
    AiMapping save(AiMapping mapping);
    long count();
}
