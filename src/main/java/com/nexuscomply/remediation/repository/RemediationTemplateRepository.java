package com.nexuscomply.remediation.repository;

import com.nexuscomply.remediation.model.RemediationTemplate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RemediationTemplateRepository {
    Page<RemediationTemplate> findAll(Pageable pageable);
    Optional<RemediationTemplate> findById(String id);
    RemediationTemplate save(RemediationTemplate template);
    long count();
}
