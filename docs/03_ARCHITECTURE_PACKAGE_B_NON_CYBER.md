# Package B — Non-Cyber API Architecture

## Layering

```text
React
  ↓
REST Controller
  ↓
Request DTO validation
  ↓
Application Service
  ↓
 ┌───────────────────────┬──────────────────────┐
 │ Mongo Repository      │ Cyber/AI Adapter      │
 │                       │                       │
 └───────────────────────┴──────────────────────┘
  ↓
MongoDB / downstream service
```

## Package structure

Suggested Spring Boot structure:

```text
com.nexuscomply
├── parser
├── normalization
├── framework
├── compliance
├── audit
├── finding
├── evidence
├── risk
├── drift
├── simulation
├── remediation
├── ai
├── report
├── dashboard
└── common
    ├── exception
    ├── validation
    ├── response
    ├── pagination
    └── security
```

Reuse the repository's actual package structure if it already differs.

## Controller responsibilities

Controllers may:

- map HTTP requests
- validate input
- call application services
- map results to response DTOs
- return correct HTTP status

Controllers must NOT:

- calculate risk
- evaluate compliance
- parse network configurations
- perform AI semantic reasoning
- directly access MongoDB
- contain complex business logic

## Service responsibilities

Services orchestrate:

- resource lifecycle
- validation beyond basic DTO validation
- repository calls
- downstream adapter calls
- transaction/consistency behavior
- API/application state transitions

## Repository responsibilities

Repositories perform:

- persistence
- retrieval
- documented query/filter operations

Repositories must not implement cybersecurity decisions.

## DTOs

Prefer:

```text
CreateXRequest
UpdateXRequest
XResponse
XSummaryResponse
XPageResponse
```

Do not expose Mongo document classes directly as API contracts.

## Adapters

When calling Cyber Engine or AI:

```text
Controller
  ↓
Application Service
  ↓
CyberEngineAdapter / AiServiceAdapter
  ↓
Stable Java interface
```

The API layer should not know internal implementation details of the engine.

## Existing code rule

Before creating any class:

```text
search repository
→ identify equivalent class
→ reuse/extend if compatible
→ avoid duplicate class
```
