package com.nexuscomply.remediation.repository;

import com.nexuscomply.remediation.model.RemediationTemplate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRemediationTemplateRepository implements RemediationTemplateRepository {

    private final Map<String, RemediationTemplate> store = new ConcurrentHashMap<>();

    public InMemoryRemediationTemplateRepository() {
        RemediationTemplate t1 = new RemediationTemplate();
        t1.setId(UUID.randomUUID().toString());
        t1.setTitle("CIS Cisco IOS-XE Baseline Remediation Template");
        store.put(t1.getId(), t1);
    }

    @Override
    public Page<RemediationTemplate> findAll(Pageable pageable) {
        List<RemediationTemplate> list = new ArrayList<>(store.values());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<RemediationTemplate> sublist = (start <= list.size()) ? list.subList(start, end) : Collections.emptyList();
        return new PageImpl<>(sublist, pageable, list.size());
    }

    @Override
    public Optional<RemediationTemplate> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public RemediationTemplate save(RemediationTemplate template) {
        if (template.getId() == null) {
            template.setId(UUID.randomUUID().toString());
        }
        store.put(template.getId(), template);
        return template;
    }

    @Override
    public long count() {
        return store.size();
    }
}
