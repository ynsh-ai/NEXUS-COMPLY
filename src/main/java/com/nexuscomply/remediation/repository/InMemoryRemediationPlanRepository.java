package com.nexuscomply.remediation.repository;

import com.nexuscomply.remediation.model.RemediationPlan;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRemediationPlanRepository implements RemediationPlanRepository {

    private final Map<String, RemediationPlan> store = new ConcurrentHashMap<>();

    public InMemoryRemediationPlanRepository() {
        RemediationPlan p1 = new RemediationPlan();
        p1.setId(UUID.randomUUID().toString());
        p1.setFindingId("find-001");
        p1.setTitle("Disable Telnet on AUX Port Plan");
        p1.setPlanStatus("DRAFT");
        p1.setCreatedAt(Instant.now());
        p1.setUpdatedAt(Instant.now());
        store.put(p1.getId(), p1);
    }

    @Override
    public Optional<RemediationPlan> findFirstByFindingId(String findingId) {
        return store.values().stream()
                .filter(p -> findingId.equalsIgnoreCase(p.getFindingId()))
                .findFirst();
    }

    @Override
    public Optional<RemediationPlan> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public RemediationPlan save(RemediationPlan plan) {
        if (plan.getId() == null) {
            plan.setId(UUID.randomUUID().toString());
        }
        if (plan.getCreatedAt() == null) {
            plan.setCreatedAt(Instant.now());
        }
        plan.setUpdatedAt(Instant.now());
        store.put(plan.getId(), plan);
        return plan;
    }

    @Override
    public long count() {
        return store.size();
    }
}
