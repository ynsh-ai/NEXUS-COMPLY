package com.nexuscomply.ai.controller;

import com.nexuscomply.ai.dto.AiDTO;
import com.nexuscomply.ai.dto.StartAnalysisRequest;
import com.nexuscomply.ai.service.AiService;

import com.nexuscomply.common.api.ApiResponse;
import com.nexuscomply.common.api.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * BUG-005, BUG-006, BUG-007, BUG-008, BUG-011, BUG-015, BUG-018 fixed.
 */
@RestController
@RequestMapping("/api/v1/ai")
@Tag(name = "AI", description = "AI analysis and mapping endpoints (B-071 → B-079)")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    // B-071 POST /api/v1/ai/analyze — BUG-007, BUG-008 fixed (async → 202)
    @PostMapping("/analyze")
    @Operation(summary = "Start an async AI analysis job")
    public ResponseEntity<ApiResponse<AiDTO>> startAnalysis(
            @Valid @RequestBody StartAnalysisRequest request) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(ApiResponse.of(aiService.startAnalysis(request)));
    }

    // B-072 GET /api/v1/ai/jobs/{id}
    @GetMapping("/jobs/{id}")
    @Operation(summary = "Poll an AI job for status")
    public ResponseEntity<ApiResponse<AiDTO>> getJob(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.of(aiService.getJob(id)));
    }

    // B-073 GET /api/v1/ai/jobs/{id}/suggestions — BUG-011 fixed (typed return)
    @GetMapping("/jobs/{id}/suggestions")
    @Operation(summary = "Get suggestions produced by an AI job")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSuggestions(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.of(aiService.getSuggestions(id)));
    }

    // B-074 GET /api/v1/ai/mappings — BUG-006 fixed
    @GetMapping("/mappings")
    @Operation(summary = "List AI mappings (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<AiDTO>>> listMappings(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.of(PageResponse.from(aiService.listMappings(pageable))));
    }

    // B-075 GET /api/v1/ai/mappings/{id}
    @GetMapping("/mappings/{id}")
    @Operation(summary = "Get a specific AI mapping")
    public ResponseEntity<ApiResponse<AiDTO>> getMapping(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.of(aiService.getMapping(id)));
    }

    // B-076 POST /api/v1/ai/mappings/{id}/approve
    @PostMapping("/mappings/{id}/approve")
    @Operation(summary = "Approve an AI mapping")
    public ResponseEntity<ApiResponse<Void>> approveMapping(@PathVariable UUID id) {
        aiService.approveMapping(id);
        return ResponseEntity.ok(ApiResponse.of(null));
    }

    // B-077 POST /api/v1/ai/mappings/{id}/reject
    @PostMapping("/mappings/{id}/reject")
    @Operation(summary = "Reject an AI mapping")
    public ResponseEntity<ApiResponse<Void>> rejectMapping(@PathVariable UUID id) {
        aiService.rejectMapping(id);
        return ResponseEntity.ok(ApiResponse.of(null));
    }

    // B-078 POST /api/v1/ai/mappings/{id}/test — BUG-011 fixed (typed return)
    @PostMapping("/mappings/{id}/test")
    @Operation(summary = "Test an AI mapping against sample data")
    public ResponseEntity<ApiResponse<Map<String, Object>>> testMapping(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.of(aiService.testMapping(id)));
    }

    // B-079 GET /api/v1/ai/mappings/{id}/usage — BUG-011 fixed (typed return)
    @GetMapping("/mappings/{id}/usage")
    @Operation(summary = "Get usage statistics for an AI mapping")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMappingUsage(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.of(aiService.getMappingUsage(id)));
    }
}
