# API Contract, HTTP, Errors, Pagination and Async Standards

## Base

```text
/api/v1
```

JSON over HTTPS.

Use `Content-Type: application/json` for JSON requests.

Use `multipart/form-data` only where the API contract requires file upload.

## Authentication

Protected endpoints receive:

```http
Authorization: Bearer <JWT>
```

Do not create a second authentication system in Package B.

Reuse Package A SecurityConfig/JWT infrastructure.

## Request ID

Every response should be traceable to a request/correlation ID.

Example:

```json
{
  "data": {},
  "requestId": "REQ-123"
}
```

For errors:

```json
{
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "Audit was not found.",
    "details": []
  },
  "requestId": "REQ-123"
}
```

## HTTP statuses

```text
200 OK
201 Created
202 Accepted
204 No Content
400 Bad Request
401 Unauthorized
403 Forbidden
404 Not Found
409 Conflict
413 Payload Too Large
422 Unprocessable Entity
429 Too Many Requests
500 Internal Server Error
502 Bad Gateway
503 Service Unavailable
```

Use 202 for genuinely asynchronous processing.

## Pagination

Collection endpoints should support a consistent model, for example:

```text
?page=0&size=20&sort=createdAt,desc
```

Response:

```json
{
  "content": [],
  "page": 0,
  "size": 20,
  "totalElements": 0,
  "totalPages": 0,
  "first": true,
  "last": true
}
```

Reuse an existing PageResponse/Page implementation if the repository already has one.

## Filtering

Use documented query parameters. Do not invent arbitrary query parameters merely for convenience.

## Async job model

Where processing is asynchronous:

```text
POST
 ↓
202 Accepted
 ↓
jobId
 ↓
GET /jobs/{jobId}
```

Statuses:

```text
QUEUED
RUNNING
COMPLETED
FAILED
CANCELLED
```

The job resource is API/application state. The underlying parser, AI, or Cyber Engine does the actual work.

## Validation

Validate:

- required fields
- enum values
- UUID/id syntax
- string lengths
- numeric ranges
- date/time values
- pagination values
- uploaded file type/size

Use Bean Validation where appropriate.

## Error ownership

Controllers/services translate known failures into stable API error codes.

Do not expose:

- stack traces
- database credentials
- internal class names
- raw Mongo errors
- AI provider secrets
- Cyber Engine internals

## API idempotency

For operations that may be retried, inspect the existing project pattern. If no pattern exists, do not invent a global idempotency mechanism without documenting it.

## REST behavior

GET must not mutate state.

DELETE should be used only where the API specification defines DELETE.

PATCH should change only the fields defined by the endpoint contract.
