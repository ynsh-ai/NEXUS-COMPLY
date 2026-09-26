# Antigravity Setup — Rules and Skills

Current Antigravity supports workspace `AGENTS.md` and `.agents/rules/*.md`; detailed rules can use `model_decision`, while `AGENTS.md` is always active for its scope. Agent Skills live under `.agents/skills/<skill>/SKILL.md` and are designed for reusable multi-step procedures.

## Recommended structure

```text
AGENTS.md

docs/
  api/
  mongodb/

.agents/
  rules/
    spring-boot-api.md
    mongodb-package-b.md
    security-and-contracts.md
    testing-and-postman.md

  skills/
    implement-package-b-api/
      SKILL.md

    verify-package-b-api/
      SKILL.md
```

## AGENTS.md

Keep it short and always-on:

```text
Project: NEXUS-COMPLY / SIH26155

Stack:
Java + Spring Boot + Spring Data MongoDB + REST/JSON.

Primary task:
Implement Package B non-cyber APIs one endpoint at a time.

Rules:
- Inspect repository first.
- Reuse Package A infrastructure.
- Do not duplicate Package A.
- Do not implement Cyber Engine algorithms.
- Do not invent API routes.
- Follow API and MongoDB documents.
- Test each endpoint before moving to the next.
- Verify persistence.
- Update Postman incrementally.
```

## Rule: Spring Boot API

Use for controller/service/repository work.

Required behavior:

- thin controllers
- DTOs
- service orchestration
- repositories for MongoDB
- centralized errors
- Bean Validation
- stable HTTP statuses
- existing project conventions first

## Rule: MongoDB

Use for document/repository/index work.

Required behavior:

- follow Mongo schema
- reuse existing configuration
- UUID convention
- `Instant`
- documented indexes
- no duplicate Package A ownership

## Rule: Security and contracts

Use for every API change.

Required behavior:

- reuse JWT/RBAC
- preserve endpoint paths
- preserve status enums
- stable errors
- no secret leakage

## Rule: Testing/Postman

Use whenever an endpoint is implemented.

Required behavior:

```text
compile
→ unit test
→ integration test
→ run API
→ Postman
→ Mongo verification
→ report
```

## Skill: implement-package-b-api

Purpose:

Implement one Package B endpoint completely.

Procedure:

1. Read endpoint contract.
2. Inspect existing code.
3. Identify dependencies.
4. Create/modify DTOs.
5. Implement service orchestration.
6. Implement repository/adapter integration.
7. Add validation/error handling.
8. Add tests.
9. Update OpenAPI.
10. Update Postman.
11. Run verification.
12. Mark endpoint complete.

## Skill: verify-package-b-api

Purpose:

Verify an already implemented endpoint.

Procedure:

1. Build.
2. Run automated tests.
3. Start required services.
4. Run Postman request(s).
5. Verify HTTP response.
6. Verify MongoDB state.
7. Verify security behavior.
8. Check logs for unexpected errors.
9. Report PASS/FAIL.
10. Do not proceed if failed.
