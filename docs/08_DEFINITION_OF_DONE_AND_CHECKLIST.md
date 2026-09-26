# Package B — Definition of Done

## Repository

```text
[ ] Actual repository inspected
[ ] Java/Spring versions confirmed
[ ] Existing architecture reused
[ ] Existing Package A dependencies identified
[ ] No duplicate security/auth infrastructure
```

## Every API

```text
[ ] Correct HTTP method
[ ] Correct `/api/v1` path
[ ] Correct request DTO
[ ] Correct response DTO
[ ] Validation
[ ] Authorization
[ ] Service implementation
[ ] Repository/adapter integration
[ ] Error mapping
[ ] Unit test
[ ] Integration/API test
[ ] Postman test
[ ] Mongo verification where persistent
[ ] OpenAPI documentation
```

## Architecture

```text
[ ] Controllers thin
[ ] Services contain orchestration
[ ] Repositories contain persistence
[ ] DTOs separate from documents
[ ] Cyber logic remains in Cyber Engine
[ ] AI semantic logic remains behind AI boundary
[ ] Package A ownership not duplicated
```

## MongoDB

```text
[ ] Collections match schema
[ ] IDs consistent
[ ] timestamps consistent
[ ] indexes created
[ ] references correct
[ ] no secrets
[ ] no unnecessary duplicate large data
```

## Security

```text
[ ] JWT from Package A reused
[ ] RBAC enforced
[ ] input validated
[ ] sensitive errors sanitized
[ ] no secrets committed
```

## Postman

```text
[ ] Environment created
[ ] IDs chained between requests
[ ] Positive tests
[ ] Negative tests
[ ] authorization tests
[ ] not-found tests
[ ] state-transition tests where applicable
[ ] full collection run passes
```

## Final

```text
[ ] B-001 through B-092 accounted for
[ ] No undocumented endpoint changes
[ ] No unresolved contract conflicts
[ ] Automated tests pass
[ ] Postman collection passes
[ ] Build passes
[ ] README updated
```
