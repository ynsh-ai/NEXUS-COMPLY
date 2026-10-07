# NEXUS-COMPLY Database Schemas

This folder contains the MongoDB JSON Schema definitions for all 19 NEXUS-COMPLY collections.

## Collections
devices, configurations, configuration_versions, normalized_configurations,
frameworks, controls, compliance_rules, audits, findings, evidence,
users, roles, risk_assessments, remediation_templates, audit_logs,
drift_events, ai_mappings, what_if_simulations, reports

## Usage
These files provide the database contract for the backend/API team.
They can be used as the reference when implementing models, repositories,
validation, and API integrations.

Note: Field requirements are based on the implemented database structure
and project schema documentation. Update them through team review if the
backend contract changes.
