# Postman + Automated Test Plan — Package B Non-Cyber APIs

## Files

```text
postman/
├── NEXUS-COMPLY-Package-B.postman_collection.json
└── NEXUS-COMPLY-local.postman_environment.json
```

## Environment variables

Minimum useful variables:

```text
baseUrl
token
deviceId
configurationId
versionId
normalizedConfigurationId
frameworkId
controlId
ruleId
auditId
findingId
evidenceId
riskId
driftId
simulationId
remediationTemplateId
remediationPlanId
aiJobId
aiMappingId
reportId
```

Use Postman scripts to capture IDs from successful responses.

Never put real secrets in the collection.

## Per-endpoint test gate

Every endpoint needs:

### Happy path

- valid request
- expected status
- expected response shape
- persistence check where relevant

### Validation

- missing required field
- invalid ID
- invalid enum
- invalid range
- invalid pagination

### Authorization

- unauthenticated
- insufficient role where applicable

### Not found

- nonexistent resource ID

### Conflict/state

Where relevant:

- invalid state transition
- duplicate operation
- already completed job

## Automated tests

Use the repository's existing stack.

Expected categories:

```text
Unit
Integration
Controller/API
Repository
Security
```

Do not introduce a second test framework unnecessarily.

## Postman progression

For each B-N:

```text
Implement
→ automated tests
→ start backend
→ Postman request
→ verify response
→ verify MongoDB
→ record result
→ next endpoint
```

## Collection organization

Folders:

```text
01 Parser
02 Normalization
03 Frameworks Controls Rules
04 Audits
05 Compliance
06 Findings Evidence
07 Risk
08 Drift
09 What-If
10 Remediation
11 AI
12 Dashboard
13 Reports
```

## Final collection run

At the end:

```text
Run full Package B collection
→ record failures
→ fix
→ rerun
→ export final collection
```
