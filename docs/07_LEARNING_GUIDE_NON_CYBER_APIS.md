# NEXUS-COMPLY — Non-Cyber API Learning Guide

Use this document to understand what you are asking Antigravity to build.

## One request

```text
Postman
  ↓
HTTP
  ↓
Controller
  ↓
DTO validation
  ↓
Service
  ↓
Repository / Adapter
  ↓
MongoDB / Cyber Engine / AI
  ↓
Service
  ↓
Response DTO
  ↓
JSON
```

## Controller

Converts HTTP into a Java method call.

Example concept:

```text
GET /api/v1/findings/{id}
```

Controller receives `id`, calls the finding service, and returns a response.

## DTO

DTO = Data Transfer Object.

It protects the API from exposing database implementation details.

Example:

```text
FindingDocument
       ↓
FindingResponse
```

## Service

The service coordinates the operation.

Example:

```text
getFinding(id)
→ validate
→ repository.findById
→ map document to response
```

For an engine-backed operation:

```text
evaluateAudit
→ load required resources
→ call ComplianceEvaluationService
→ persist returned result
→ return response
```

## Repository

The repository handles MongoDB access.

Example:

```text
FindingRepository
→ findByAuditId
→ findByDeviceId
→ findBySeverity
```

It does not decide whether a security configuration is compliant.

## Adapter

An adapter isolates downstream systems.

Example:

```text
ComplianceService
    ↓
ComplianceEngineAdapter
    ↓
ComplianceEvaluationService
```

## Async job

Some operations take time.

```text
POST /parse
→ 202 + jobId

GET /parse-jobs/{jobId}
→ status
```

The API tracks the job. The engine performs the actual work.

## MongoDB

Think of MongoDB documents as persistent resource records.

Package B resources include:

```text
Audit
Finding
Evidence
RiskAssessment
DriftEvent
Simulation
RemediationPlan
AiMapping
Report
```

## API completion

An endpoint is complete only when:

```text
Code
+
Tests
+
Correct HTTP behavior
+
Persistence behavior
+
Postman verification
+
Documentation
```

## What Antigravity should NOT do

Do not let it:

- invent new API paths
- invent duplicate authentication
- duplicate Package A models
- put MongoDB access in controllers
- put cyber algorithms in controllers
- use AI to make final compliance decisions
- change the Mongo schema silently
- declare success without tests
