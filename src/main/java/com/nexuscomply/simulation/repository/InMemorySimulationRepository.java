package com.nexuscomply.simulation.repository;

import com.nexuscomply.simulation.model.Simulation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemorySimulationRepository implements SimulationRepository {

    private final Map<String, Simulation> store = new ConcurrentHashMap<>();

    @Override
    public Simulation save(Simulation sim) {
        if (sim.getId() == null) {
            sim.setId(UUID.randomUUID().toString());
        }
        if (sim.getCreatedAt() == null) {
            sim.setCreatedAt(Instant.now());
        }
        sim.setUpdatedAt(Instant.now());
        store.put(sim.getId(), sim);
        return sim;
    }

    @Override
    public Page<Simulation> findAll(Pageable pageable) {
        List<Simulation> list = new ArrayList<>(store.values());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<Simulation> sublist = (start <= list.size()) ? list.subList(start, end) : Collections.emptyList();
        return new PageImpl<>(sublist, pageable, list.size());
    }

    @Override
    public Optional<Simulation> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public boolean existsById(String id) {
        return store.containsKey(id);
    }

    @Override
    public void deleteById(String id) {
        store.remove(id);
    }

    @Override
    public long count() {
        return store.size();
    }
}
