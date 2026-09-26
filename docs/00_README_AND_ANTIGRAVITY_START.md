# NEXUS-COMPLY — Non-Cyber API Antigravity Documentation Bundle

## Purpose

This bundle is the implementation contract for the **API/application layer** of NEXUS-COMPLY Package B.

It covers REST controllers, DTOs, services/orchestration, MongoDB repositories, validation, pagination, error handling, async job tracking, Postman verification, tests, and frontend integration.

It does **not** authorize implementation of Cyber Engine algorithms.

## Source of truth

Use these together:

1. Package B API PDF.
2. Complete API Functionality Specification.
3. Package B Antigravity Master Guide.
4. MongoDB Package B Schema.
5. The actual repository.
6. The existing Package A code/contracts.

If the repository already has a compatible implementation, reuse it.

If two documents conflict, STOP and report the conflict. Do not silently invent a third contract.

## Core rule

Implement one endpoint at a time:

```text
B-001
→ compile
→ unit/integration tests
→ API verification
→ MongoDB verification
→ Postman verification
→ mark complete
→ B-002
```

Do not batch-generate all controllers and call the work complete.

## Non-cyber boundary

This bundle owns:

- REST API surface
- request/response DTOs
- validation
- authentication/authorization integration
- service orchestration
- repository persistence
- filtering/pagination
- async job resources
- report metadata/file handling
- API error handling
- Postman
- tests
- OpenAPI documentation

This bundle does NOT own:

- vendor parser algorithms
- configuration normalization algorithms
- compliance decision algorithms
- evidence derivation algorithms
- risk formulas
- drift algorithms
- what-if calculation algorithms
- remediation semantic validation
- AI semantic interpretation

Those capabilities must be consumed through stable interfaces.

## Antigravity first instruction

Before writing code:

1. Inspect the full repository.
2. Identify Spring Boot version and Java version.
3. Identify existing Package A modules.
4. Identify existing common response/error/security classes.
5. Identify existing Mongo configuration.
6. Identify existing testing conventions.
7. Identify existing API routes.
8. Produce a short dependency/conflict report.
9. Only then start B-001.
