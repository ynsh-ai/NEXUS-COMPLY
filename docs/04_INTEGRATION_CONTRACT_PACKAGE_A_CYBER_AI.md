# Integration Contract — Package A, Cyber Engine and AI

## Package A dependency

Package B consumes Package A resources.

Primary IDs:

```text
userId
deviceId
configurationId
versionId
vendorDetectionId
```

Do not duplicate Package A ownership.

## Package A shared infrastructure

Reuse if present:

```text
SecurityConfig
JWT filter/provider
UserDetails
ApiResponse
ErrorResponse
Global exception handler
Request/correlation ID filter
PageResponse
base timestamp/audit classes
Mongo configuration
OpenAPI configuration
```

If missing:

1. Report it.
2. Determine whether Package A is already implementing it.
3. Do not silently create a competing implementation.
4. Use a minimal adapter/stub only when needed to compile Package B.

## Cyber Engine boundary

The API layer calls stable Java interfaces.

Expected capability families:

```text
ParserService
NormalizationService
ComplianceEvaluationService
FindingGenerationService
RiskCalculationService
DriftDetectionService
WhatIfSimulationService
RemediationValidationService
```

The exact package names and method signatures must come from the actual Cyber Engine contract.

If the engine is not ready, create interfaces + mock implementations for API tests.

Do not implement real cybersecurity algorithms as a substitute.

## AI boundary

Expected capability:

```text
AiAnalysisService
```

AI API layer responsibilities:

- submit job
- track job
- retrieve suggestions
- approve/reject mappings
- test mapping
- read usage

AI layer does the semantic analysis.

Package B handles resource lifecycle and persistence.

## Contract requirements

For every adapter/interface, obtain or define:

```text
request DTO
response DTO
method signature
sync/async behavior
status enum
exception/error mapping
timeout behavior
```

## Downstream failure

Map downstream failures to stable API errors.

Do not expose raw exceptions.

Example:

```text
Cyber Engine unavailable
→ 503 SERVICE_UNAVAILABLE

Cyber Engine returned invalid response
→ 502 BAD_GATEWAY

Requested resource does not exist
→ 404 RESOURCE_NOT_FOUND
```

## Contract conflict rule

If Package A or Cyber Engine gives a contract that differs from this document or the API PDFs:

STOP.

Report:

```text
existing contract
expected contract
conflict
recommended resolution
```

Do not silently change routes or DTOs.
