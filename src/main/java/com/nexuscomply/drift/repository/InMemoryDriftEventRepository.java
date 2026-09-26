package com.nexuscomply.drift.repository;

import com.nexuscomply.drift.model.DriftEvent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryDriftEventRepository implements DriftEventRepository {

    private final Map<String, DriftEvent> store = new ConcurrentHashMap<>();

    public InMemoryDriftEventRepository() {
        DriftEvent d1 = new DriftEvent();
        d1.setId(UUID.randomUUID().toString());
        d1.setDeviceId("dev-001");
        d1.setDescription("Configuration drift detected in AAA subsystem");
        d1.setDriftStatus("DETECTED");
        d1.setDetectedAt(Instant.now());
        store.put(d1.getId(), d1);
    }

    @Override
    public Page<DriftEvent> findAll(Pageable pageable) {
        return toPage(new ArrayList<>(store.values()), pageable);
    }

    @Override
    public Optional<DriftEvent> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Page<DriftEvent> findByDeviceId(String deviceId, Pageable pageable) {
        List<DriftEvent> list = store.values().stream()
                .filter(d -> deviceId.equalsIgnoreCase(d.getDeviceId()))
                .collect(Collectors.toList());
        return toPage(list, pageable);
    }

    @Override
    public Page<DriftEvent> findAllByOrderByDetectedAtDesc(Pageable pageable) {
        List<DriftEvent> list = store.values().stream()
                .sorted(Comparator.comparing(DriftEvent::getDetectedAt, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
        return toPage(list, pageable);
    }

    @Override
    public DriftEvent save(DriftEvent event) {
        if (event.getId() == null) {
            event.setId(UUID.randomUUID().toString());
        }
        if (event.getDetectedAt() == null) {
            event.setDetectedAt(Instant.now());
        }
        store.put(event.getId(), event);
        return event;
    }

    @Override
    public long count() {
        return store.size();
    }

    private Page<DriftEvent> toPage(List<DriftEvent> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<DriftEvent> sublist = (start <= list.size()) ? list.subList(start, end) : Collections.emptyList();
        return new PageImpl<>(sublist, pageable, list.size());
    }
}
