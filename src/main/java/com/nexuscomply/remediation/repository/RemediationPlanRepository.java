package com.nexuscomply.remediation.repository;

import com.nexuscomply.remediation.model.RemediationPlan;


import java.util.Optional;

public interface RemediationPlanRepository {
    Optional<RemediationPlan> findFirstByFindingId(String findingId);
    Optional<RemediationPlan> findById(String id);
    RemediationPlan save(RemediationPlan plan);
    long count();
}
