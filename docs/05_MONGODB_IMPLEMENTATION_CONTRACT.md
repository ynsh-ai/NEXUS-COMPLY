# MongoDB Implementation Contract — Package B

Use the separate:

`NEXUS_COMPLY_MONGODB_SCHEMA_PACKAGE_B.md`

as the field-level schema.

## Package B collections

```text
normalized_configurations
frameworks
controls
compliance_rules
audits
findings
evidence
risk_assessments
drift_events
what_if_simulations
remediation_templates
remediation_plans
ai_mappings
ai_jobs
reports
```

## Package A collections referenced, not re-owned

```text
users
roles
devices
configurations
configuration_versions
vendor_detections
audit_logs
```

## Rules

- Use Spring Data MongoDB.
- Use consistent domain IDs.
- Use `Instant` for timestamps.
- Keep Mongo documents separate from REST DTOs.
- Define indexes for documented queries.
- Do not duplicate large configuration content unnecessarily.
- Use references for independently managed resources.
- Embed small value structures where appropriate.
- Do not store secrets.
- Do not create a second database architecture.
- Do not add PostgreSQL merely because older proposal documents mentioned it.

## Schema change process

Before adding a field:

```text
1. Check API contract.
2. Check frontend requirement.
3. Check Cyber Engine contract.
4. Check existing Mongo document.
5. Document why the field is needed.
6. Add repository/entity test where relevant.
```

If a field is required but not specified anywhere, report the gap before silently deciding.

## Persistence verification

For every persistent endpoint:

```text
API request
→ response
→ inspect Mongo document
→ repeat GET
→ confirm response matches persisted state
```
