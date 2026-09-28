package com.nexuscomply.risk.repository;

import com.nexuscomply.risk.model.RiskAssessment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RiskAssessmentRepository {
    Page<RiskAssessment> findAll(Pageable pageable);
    Page<RiskAssessment> findAllByDeviceIdNotNull(Pageable pageable);
    Page<RiskAssessment> findAllByFindingIdNotNull(Pageable pageable);
    Optional<RiskAssessment> findFirstByDeviceId(String deviceId);
    Optional<RiskAssessment> findFirstByFindingId(String findingId);
    List<RiskAssessment> findAllByOrderByCalculatedAtDesc(Pageable pageable);
    Page<RiskAssessment> findAllByAuditId(String auditId, Pageable pageable);
    RiskAssessment save(RiskAssessment assessment);
    Optional<RiskAssessment> findById(String id);
    long count();
}
