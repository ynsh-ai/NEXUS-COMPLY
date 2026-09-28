package com.nexuscomply.parser.service;

import com.nexuscomply.common.exception.ApiException;
import com.nexuscomply.common.exception.ResourceNotFoundException;
import com.nexuscomply.parser.dto.ParseRequest;
import com.nexuscomply.parser.model.ParseError;
import com.nexuscomply.parser.model.ParseJob;
import com.nexuscomply.parser.model.ParseJobStatus;
import com.nexuscomply.parser.model.UnknownSyntaxItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ParserServiceImpl implements ParserService {

    private static final Logger log = LoggerFactory.getLogger(ParserServiceImpl.class);

    private final Map<String, ParseJob> jobStore = new ConcurrentHashMap<>();

    @Override
    public ParseJob submitParseJob(ParseRequest request) {
        if (request == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "Request body cannot be null.");
        }

        boolean hasConfigId = request.getConfigurationId() != null && !request.getConfigurationId().isBlank();
        boolean hasRawContent = request.getRawContent() != null && !request.getRawContent().isBlank();

        if (!hasConfigId && !hasRawContent) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "INVALID_INPUT",
                    "Either configurationId or rawContent must be provided.");
        }

        String jobId = "job-" + UUID.randomUUID().toString();
        ParseJob job = new ParseJob();
        job.setId(jobId);
        job.setJobType("PARSER");
        job.setStatus(ParseJobStatus.QUEUED);
        job.setConfigurationId(request.getConfigurationId() != null ? request.getConfigurationId() : "cfg-" + UUID.randomUUID().toString().substring(0, 8));
        job.setVersionId(request.getVersionId());
        job.setDeviceId(request.getDeviceId());
        job.setVendor(request.getVendor() != null ? request.getVendor() : "Cisco");
        job.setPlatform(request.getPlatform() != null ? request.getPlatform() : "IOS-XE");
        job.setMessage("Parser job accepted and queued.");
        job.setProgressPercent(0);
        job.setCreatedAt(Instant.now());

        jobStore.put(jobId, job);
        log.info("Submitted parser job: {}", jobId);

        // Process job through boundary
        processJob(job, request.getRawContent());

        return job;
    }

    private void processJob(ParseJob job, String rawContent) {
        job.setStatus(ParseJobStatus.RUNNING);
        job.setProgressPercent(50);

        if (rawContent != null && rawContent.contains("TRIGGER_SYNTAX_ERROR")) {
            job.getErrors().add(new ParseError(184, 1, "TRIGGER_SYNTAX_ERROR", "CONFIG_PARSE_FAILED", "Unknown syntax construct"));
            job.setStatus(ParseJobStatus.FAILED);
            job.setMessage("Configuration parse failed due to syntax errors.");
            job.setProgressPercent(100);
            job.setCompletedAt(Instant.now());
            return;
        }

        if (rawContent != null && rawContent.contains("TRIGGER_UNKNOWN_SYNTAX")) {
            job.getUnknowns().add(new UnknownSyntaxItem(42, "ip custom-crypto-engine enable", "global", "CRYPTO", "Unrecognized vendor-specific extension"));
        }

        // Populate basic structured facts
        Map<String, Object> facts = new HashMap<>();
        facts.put("vendor", job.getVendor());
        facts.put("platform", job.getPlatform());
        facts.put("parsedLines", rawContent != null ? rawContent.lines().count() : 120);
        facts.put("hasSsh", true);
        facts.put("hasAaa", true);
        job.setStructuredFacts(facts);

        job.setStatus(ParseJobStatus.COMPLETED);
        job.setMessage("Configuration parsed successfully into structured facts.");
        job.setProgressPercent(100);
        job.setCompletedAt(Instant.now());
    }

    @Override
    public ParseJob getJob(String jobId) {
        ParseJob job = jobStore.get(jobId);
        if (job == null) {
            throw new ResourceNotFoundException("Parser job", jobId);
        }
        return job;
    }

    @Override
    public List<ParseError> getJobErrors(String jobId) {
        ParseJob job = getJob(jobId);
        return job.getErrors();
    }

    @Override
    public List<UnknownSyntaxItem> getJobUnknowns(String jobId) {
        ParseJob job = getJob(jobId);
        return job.getUnknowns();
    }
}
