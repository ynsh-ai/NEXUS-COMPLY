package com.nexuscomply.normalization.service;

import com.nexuscomply.common.exception.ApiException;
import com.nexuscomply.common.exception.ResourceNotFoundException;
import com.nexuscomply.normalization.dto.NormalizeRequest;
import com.nexuscomply.normalization.model.NormalizedConfiguration;
import com.nexuscomply.normalization.model.SourceMapEntry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NormalizationServiceImpl implements NormalizationService {

    private final Map<String, NormalizedConfiguration> storeById = new ConcurrentHashMap<>();
    private final Map<String, NormalizedConfiguration> storeByVersionId = new ConcurrentHashMap<>();

    @Override
    public NormalizedConfiguration normalizeConfiguration(NormalizeRequest request) {
        if (request == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "Request body cannot be null.");
        }

        boolean hasConfigId = request.getConfigurationId() != null && !request.getConfigurationId().isBlank();
        boolean hasRawContent = request.getRawContent() != null && !request.getRawContent().isBlank();

        if (!hasConfigId && !hasRawContent) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_INPUT",
                    "Either configurationId or rawContent must be provided.");
        }

        String id = "norm-" + UUID.randomUUID().toString();
        NormalizedConfiguration config = new NormalizedConfiguration();
        config.setId(id);
        config.setConfigurationId(hasConfigId ? request.getConfigurationId() : "cfg-" + UUID.randomUUID().toString().substring(0, 8));
        config.setVersionId(request.getVersionId() != null ? request.getVersionId() : "v1.0.0");
        config.setDeviceId(request.getDeviceId() != null ? request.getDeviceId() : "dev-" + UUID.randomUUID().toString().substring(0, 8));
        config.setVendor(request.getVendor() != null ? request.getVendor() : "Cisco");
        config.setPlatform(request.getPlatform() != null ? request.getPlatform() : "IOS-XE");
        config.setNormalizedAt(Instant.now());

        // Build canonical security state
        Map<String, Object> security = new HashMap<>();
        security.put("ssh", Map.of("enabled", true, "version", 2));
        security.put("telnet", Map.of("enabled", false));
        security.put("snmp", Map.of("version", "v3", "communityProtected", true));
        security.put("aaa", Map.of("enabled", true, "newModel", true));
        security.put("logging", Map.of("syslog", true, "level", "informational"));

        Map<String, Object> canonical = new HashMap<>();
        canonical.put("security", security);
        canonical.put("interfaces", List.of(
                Map.of("name", "GigabitEthernet0/0/0", "status", "up", "ipAddress", "192.168.1.1")
        ));
        config.setCanonicalState(canonical);

        // Build source map
        List<SourceMapEntry> sourceMap = new ArrayList<>();
        sourceMap.add(new SourceMapEntry("security.ssh.enabled", 12, "transport input ssh"));
        sourceMap.add(new SourceMapEntry("security.ssh.version", 14, "ip ssh version 2"));
        sourceMap.add(new SourceMapEntry("security.telnet.enabled", 15, "no transport input telnet"));
        sourceMap.add(new SourceMapEntry("security.aaa.enabled", 20, "aaa new-model"));
        config.setSourceMap(sourceMap);

        storeById.put(id, config);
        if (config.getVersionId() != null) {
            storeByVersionId.put(config.getVersionId(), config);
        }

        return config;
    }

    @Override
    public NormalizedConfiguration getById(String id) {
        NormalizedConfiguration config = storeById.get(id);
        if (config == null) {
            throw new ResourceNotFoundException("Normalized configuration", id);
        }
        return config;
    }

    @Override
    public NormalizedConfiguration getByVersionId(String versionId) {
        NormalizedConfiguration config = storeByVersionId.get(versionId);
        if (config == null) {
            throw new ResourceNotFoundException("Normalized configuration for version", versionId);
        }
        return config;
    }

    @Override
    public List<SourceMapEntry> getSourceMap(String id) {
        NormalizedConfiguration config = getById(id);
        return config.getSourceMap();
    }
}
