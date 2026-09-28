package com.nexuscomply.drift.repository;

import com.nexuscomply.drift.model.DriftEvent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DriftEventRepository {
    Page<DriftEvent> findAll(Pageable pageable);
    Optional<DriftEvent> findById(String id);
    Page<DriftEvent> findByDeviceId(String deviceId, Pageable pageable);
    Page<DriftEvent> findAllByOrderByDetectedAtDesc(Pageable pageable);
    DriftEvent save(DriftEvent event);
    long count();
}
