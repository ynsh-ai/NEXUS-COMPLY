package com.nexuscomply.simulation.repository;

import com.nexuscomply.simulation.model.Simulation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SimulationRepository {
    Simulation save(Simulation sim);
    Page<Simulation> findAll(Pageable pageable);
    Optional<Simulation> findById(String id);
    boolean existsById(String id);
    void deleteById(String id);
    long count();
}
