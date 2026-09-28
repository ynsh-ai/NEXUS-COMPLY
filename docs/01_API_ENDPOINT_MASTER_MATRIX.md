# Package B — Non-Cyber API Endpoint Master Matrix

Base path:

```text
/api/v1
```

All endpoints are REST/JSON unless the endpoint explicitly uses multipart upload or binary download.

## Implementation sequence

### Parser
- B-001 POST `/cyber/parse`
- B-002 GET `/cyber/parse-jobs/{jobId}`
- B-003 GET `/cyber/parse-jobs/{jobId}/errors`
- B-004 GET `/cyber/parse-jobs/{jobId}/unknowns`

### Normalization
- B-005 POST `/cyber/normalize`
- B-006 GET `/normalized-configurations/{id}`
- B-007 GET `/configurations/versions/{id}/normalized`
- B-008 GET `/normalized-configurations/{id}/source-map`

### Frameworks / Controls / Rules
- B-009 GET `/frameworks`
- B-010 GET `/frameworks/{id}`
- B-011 GET `/frameworks/{id}/controls`
- B-012 GET `/controls/{id}`
- B-013 GET `/controls/{id}/rules`
- B-014 GET `/rules/{id}`
- B-015 GET `/framework-mappings`
- B-016 GET `/framework-mappings/{id}`

### Audits
- B-017 POST `/audits`
- B-018 GET `/audits`
- B-019 GET `/audits/{id}`
- B-020 GET `/audits/{id}/status`
- B-021 POST `/audits/{id}/cancel`
- B-022 POST `/audits/{id}/rerun`
- B-023 GET `/audits/{id}/summary`
- B-024 GET `/audits/{id}/framework-results`
- B-025 GET `/audits/{id}/findings`
- B-026 GET `/audits/{id}/risk`
- B-027 POST `/audits/{id}/report`

### Compliance
- B-028 POST `/compliance/evaluate`
- B-029 POST `/compliance/evaluate/control/{id}`
- B-030 POST `/compliance/evaluate/framework/{id}`
- B-031 GET `/compliance/results/{auditId}`
- B-032 GET `/compliance/results/{auditId}/summary`
- B-033 GET `/compliance/results/{auditId}/unknowns`

### Findings / Evidence
- B-034 GET `/findings`
- B-035 GET `/findings/{id}`
- B-036 PATCH `/findings/{id}/status`
- B-037 PATCH `/findings/{id}/severity`
- B-038 GET `/findings/{id}/history`
- B-039 GET `/findings/{id}/related`
- B-040 POST `/findings/{id}/acknowledge`
- B-041 POST `/findings/{id}/resolve`
- B-042 GET `/findings/{id}/evidence`
- B-043 GET `/evidence/{id}`
- B-044 GET `/evidence/{id}/source`
- B-045 GET `/evidence/{id}/configuration`

### Risk
- B-046 GET `/risk`
- B-047 GET `/risk/findings`
- B-048 GET `/risk/devices`
- B-049 GET `/risk/devices/{id}`
- B-050 GET `/risk/trend`
- B-051 POST `/risk/recalculate/{auditId}`
- B-052 GET `/risk/{findingId}`

### Drift
- B-053 GET `/drift`
- B-054 GET `/drift/{id}`
- B-055 GET `/devices/{id}/drift`
- B-056 POST `/drift/compare`
- B-057 GET `/drift/{id}/affected-controls`
- B-058 GET `/drift/{id}/risk-impact`

### What-If
- B-059 POST `/simulations`
- B-060 GET `/simulations`
- B-061 GET `/simulations/{id}`
- B-062 POST `/simulations/{id}/rerun`
- B-063 DELETE `/simulations/{id}`

### Remediation
- B-064 GET `/remediation/templates`
- B-065 GET `/remediation/templates/{id}`
- B-066 GET `/findings/{id}/remediation`
- B-067 POST `/findings/{id}/remediation/plan`
- B-068 POST `/remediation/plans/{id}/validate`
- B-069 GET `/remediation/plans/{id}`
- B-070 POST `/remediation/plans/{id}/verify`

### AI
- B-071 POST `/ai/analyze`
- B-072 GET `/ai/jobs/{id}`
- B-073 GET `/ai/jobs/{id}/suggestions`
- B-074 GET `/ai/mappings`
- B-075 GET `/ai/mappings/{id}`
- B-076 POST `/ai/mappings/{id}/approve`
- B-077 POST `/ai/mappings/{id}/reject`
- B-078 POST `/ai/mappings/{id}/test`
- B-079 GET `/ai/mappings/{id}/usage`

### Dashboard
- B-080 GET `/dashboard/summary`
- B-081 GET `/dashboard/compliance`
- B-082 GET `/dashboard/findings`
- B-083 GET `/dashboard/risk`
- B-084 GET `/dashboard/drift`
- B-085 GET `/dashboard/activity`
- B-086 GET `/dashboard/frameworks`

### Reports
- B-087 POST `/reports`
- B-088 GET `/reports`
- B-089 GET `/reports/{id}`
- B-090 GET `/reports/{id}/preview`
- B-091 GET `/reports/{id}/download`
- B-092 POST `/reports/{id}/regenerate`

## Completion state

Each endpoint must have:

```text
[ ] Controller
[ ] Request DTO(s)
[ ] Response DTO(s)
[ ] Validation
[ ] Service method
[ ] Repository/query if needed
[ ] Cyber Engine/AI adapter if needed
[ ] Error handling
[ ] Unit test
[ ] Integration test
[ ] Postman request
[ ] MongoDB verification if persistent
[ ] OpenAPI documentation
[ ] Endpoint verified
```

Do not mark complete merely because the controller compiles.
