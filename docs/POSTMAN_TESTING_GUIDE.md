# NEXUS-COMPLY pi2 — Complete Postman Testing Guide

> **For the person receiving this project.**
> This guide walks you through setting up and testing every API endpoint, one by one, in Postman.
> No prior knowledge of the codebase is needed — just follow each step in order.

---

## PART 1 — Prerequisites (Do This Once)

### 1.1 — Install Java 17
- Download: https://adoptium.net/temurin/releases/?version=17
- Verify: `java -version` → should show `openjdk version "17.x.x"`

### 1.2 — Install Maven
- Download: https://maven.apache.org/download.cgi
- Verify: `mvn -version`

### 1.3 — Install MongoDB
- Download: https://www.mongodb.com/try/download/community
- Install with default settings (runs on port **27017**)
- Windows: runs as a service automatically
- Verify: `mongosh --eval "db.runCommand({ connectionStatus: 1 })"`

### 1.4 — Install Postman
- Download: https://www.postman.com/downloads/
- Install and open (no account needed)

---

## PART 2 — Start the Server

Open a terminal in the `pi2` folder and run:

```
mvn spring-boot:run
```

Wait until you see:
```
Started Application in X.XXX seconds
Tomcat started on port(s): 8080 (http)
```

> Keep this terminal open the entire time you test.

---

## PART 3 — Set Up Postman

### 3.1 — Import the Collection
1. Open Postman → click **Import** (top-left)
2. Select **"Upload Files"**
3. Select the files from the `postman/` directory:
   - `postman/NEXUS-COMPLY-Full-Suite-B001-B092.postman_collection.json`
   - `postman/NEXUS-COMPLY-local.postman_environment.json`
4. Click **Import**

You will see **"NEXUS-COMPLY Full Suite (B-001 to B-092)"** in your Collections sidebar and **"NEXUS-COMPLY-local"** in your Environments dropdown.

### 3.2 — Collection Variables (pre-set)
Click the collection → **Variables** tab or select the environment. These are already set:

| Variable | Value |
|---|---|
| `baseUrl` / `base` | `http://localhost:8080/api/v1` |
| `simId` | *(auto-saved when you create a simulation)* |
| `driftId` | *(paste after drift compare)* |
| `planId` | *(paste after creating a plan)* |
| `reportId` | *(auto-saved when you create a report)* |
| `aiJobId` | *(auto-saved when you start AI analysis)* |

---

## PART 4 — Smoke Tests (Run These First!)

Open folder: **"Quick Smoke Tests"**

| # | Test | Click & Send | Expected |
|---|---|---|---|
| 1 | Server health | SMOKE: GET dashboard/summary | 200 OK — all counts zero |
| 2 | Empty list | SMOKE: GET risk | 200 OK — `content: []` |
| 3 | Create resource | SMOKE: POST simulation | **201 Created** — simId saved |
| 4 | Async job | SMOKE: POST ai/analyze | **202 Accepted** — status QUEUED |
| 5 | Validation | SMOKE: Validation error | **400 Bad Request** — VALIDATION_FAILED |
| 6 | Not found | SMOKE: 404 Not Found | **404 Not Found** — RESOURCE_NOT_FOUND |

All 6 pass? Continue below.

---

## PART 5 — Simulations (B-059 to B-063)

Open folder: **"What-If Simulations"**

### Step 5.1 — Create Simulation (DO THIS FIRST)
- Click **"B-059 Create Simulation"**
- Body is pre-filled. Click **Send**
- Expected: **201 Created**
- `simId` is automatically saved.

### Step 5.2 — List All Simulations
- Click **"B-060 List Simulations"** → Send
- Expected: **200 OK** — your simulation appears in `content`

### Step 5.3 — Get by ID
- Click **"B-061 Get Simulation by ID"** → Send
- Expected: **200 OK** — status is "QUEUED"

### Step 5.4 — Rerun
- Click **"B-062 Rerun Simulation"** → Send
- Expected: **200 OK** — status reset to "QUEUED"

### Step 5.5 — Delete
- Click **"B-063 Delete Simulation"** → Send
- Expected: **204 No Content** (empty body)
- Then run Step 5.3 again → should return **404 Not Found** ✅

---

## PART 6 — Drift Detection (B-053 to B-058)

Open folder: **"Drift"**

### Step 6.1 — List Drift Events
- Click **"B-053 List Drift Events"** → Send
- Expected: **200 OK**, empty list

### Step 6.2 — Get Drift (404 Test)
- Click **"B-054 Get Drift Event by ID"** → Send
- Expected: **404 Not Found** ✅

### Step 6.3 — Device Drift
- Click **"B-055 Get Device Drift"** → Send
- Expected: **200 OK**, empty list

### Step 6.4 — Compare Versions (Creates a DriftEvent)
- Click **"B-056 Compare Versions"** → Send
- Body already has baseConfigId and targetConfigId
- Expected: **201 Created**
- **IMPORTANT:** Copy `data.id` from the response → paste into `driftId` variable (Collection → Variables → driftId)

### Step 6.5 — Affected Controls
- Click **"B-057 Get Affected Controls for Drift"** → Send
- Expected: **200 OK** — `"affectedControls": []`

### Step 6.6 — Risk Impact
- Click **"B-058 Get Risk Impact for Drift"** → Send
- Expected: **200 OK**

---

## PART 7 — Remediation (B-064 to B-070)

Open folder: **"Remediation"**

### Step 7.1 — List Templates
- Click **"B-064 List Remediation Templates"** → Send
- Expected: **200 OK**, empty list

### Step 7.2 — Get Template (404 Test)
- Click **"B-065 Get Remediation Template by ID"** → Send
- Expected: **404 Not Found** ✅

### Step 7.3 — Get Finding Remediation (404 Test)
- Click **"B-066 Get Remediation for Finding"** → Send
- Expected: **404 Not Found** (no plan exists yet)

### Step 7.4 — Create Remediation Plan (DO THIS FIRST)
- Click **"B-067 Create Remediation Plan"** → Send
- Expected: **201 Created** — `"planStatus": "DRAFT"`
- **Copy `data.id`** → paste into `planId` variable

### Step 7.5 — Validate Plan
- Click **"B-068 Validate Remediation Plan"** → Send
- Expected: **200 OK** — `"status": "VALIDATED"`, `"valid": true`

### Step 7.6 — Get Plan by ID
- Click **"B-069 Get Remediation Plan by ID"** → Send
- Expected: **200 OK** — `"planStatus": "VALIDATED"` (changed from DRAFT)

### Step 7.7 — Verify Plan
- Click **"B-070 Verify Remediation Plan"** → Send
- Expected: **200 OK** — `"status": "VERIFIED"`, `"verified": true`

---

## PART 8 — AI Analysis (B-071 to B-079)

Open folder: **"AI"**

### Step 8.1 — Start Analysis (Async)
- Click **"B-071 Start AI Analysis"** → Send
- Expected: **202 Accepted** — `"status": "QUEUED"`
- `aiJobId` is automatically saved ✅

### Step 8.2 — Poll Job Status
- Click **"B-072 Get AI Job Status"** → Send
- Expected: **200 OK** — `"status": "QUEUED"`

### Step 8.3 — Get Suggestions
- Click **"B-073 Get AI Job Suggestions"** → Send
- Expected: **200 OK** — `"suggestions": []`

### Step 8.4 — List Mappings
- Click **"B-074 List AI Mappings"** → Send
- Expected: **200 OK**, empty list

### Steps 8.5 to 8.9 — Mapping Operations (404 Tests)
B-075, B-076, B-077, B-078, B-079 all use a fake UUID → all return **404 Not Found** ✅

---

## PART 9 — Reports (B-087 to B-092)

Open folder: **"Reports"**

### Step 9.1 — Create Report (DO THIS FIRST)
- Click **"B-087 Create Report"** → Send
- Body: name, auditId, format already filled
- Expected: **201 Created** — `"status": "QUEUED"`
- `reportId` is automatically saved ✅

### Step 9.2 — List Reports
- Click **"B-088 List Reports"** → Send
- Expected: **200 OK** — shows your report

### Step 9.3 — Get by ID
- Click **"B-089 Get Report by ID"** → Send
- Expected: **200 OK** — `"status": "QUEUED"`

### Step 9.4 — Preview
- Click **"B-090 Preview Report"** → Send
- Expected: **200 OK**

### Step 9.5 — Download (Business Rule Test)
- Click **"B-091 Download Report"** → Send
- Expected: **500 Internal Server Error** — this is CORRECT!
- Reason: Report is still "QUEUED" (not ready). The guard is working.

### Step 9.6 — Regenerate
- Click **"B-092 Regenerate Report"** → Send
- Expected: **200 OK** — status reset to "QUEUED"

---

## PART 10 — Risk (B-046 to B-052)

Open folder: **"Risk"**

| Step | Request | Expected |
|---|---|---|
| 10.1 | B-046 List All Risks | 200 OK, empty |
| 10.2 | B-047 List by Findings | 200 OK, empty |
| 10.3 | B-048 List by Devices | 200 OK, empty |
| 10.4 | B-049 Get Risk for Device | **404 Not Found** |
| 10.5 | B-050 Risk Trend | 200 OK, empty |
| 10.6 | B-051 Recalculate Risk | **202 Accepted** |
| 10.7 | B-052 Get Risk for Finding | **404 Not Found** |

---

## PART 11 — Dashboard (B-080 to B-086)

Open folder: **"Dashboard"** — all are simple GET, no body needed.

| Endpoint | URL | What You See |
|---|---|---|
| B-080 Summary | /dashboard/summary | Total counts |
| B-081 Compliance | /dashboard/compliance | Placeholder message |
| B-082 Findings | /dashboard/findings | Placeholder message |
| B-083 Risk | /dashboard/risk | Risk count |
| B-084 Drift | /dashboard/drift | Drift event count |
| B-085 Activity | /dashboard/activity | Activity counts |
| B-086 Frameworks | /dashboard/frameworks | Placeholder message |

TIP: Run B-080 LAST — after Parts 5-10, it will show real non-zero counts!

---

## PART 12 — Validation Error Tests

Test that bad input is correctly rejected:

| Test | What to Send | Expected |
|---|---|---|
| POST /simulations with `{}` | Empty body | 400 — VALIDATION_FAILED |
| POST /simulations with `{"name":""}` | Empty name | 400 — VALIDATION_FAILED |
| POST /simulations with `{"name":"AB"}` | Name too short | 400 — VALIDATION_FAILED |
| POST /drift/compare with one field | Missing targetConfigId | 400 — VALIDATION_FAILED |
| POST /ai/analyze with `{}` | Missing targetId | 400 — VALIDATION_FAILED |

---

## PART 13 — Swagger UI (Browser Alternative)

You can also test everything in your browser without Postman:

1. Server must be running (Part 2)
2. Open: http://localhost:8080/swagger-ui.html
3. Click any endpoint → "Try it out" → fill fields → "Execute"

---

## Expected Response Formats

### Success
```json
{
  "data": { ...fields... },
  "requestId": "REQ-..."
}
```

### Paginated List
```json
{
  "data": {
    "content": [...],
    "page": 0,
    "size": 20,
    "totalElements": 5,
    "totalPages": 1,
    "last": true
  },
  "requestId": "REQ-..."
}
```

### Error
```json
{
  "error": {
    "code": "RESOURCE_NOT_FOUND",
    "message": "Simulation with id 'abc' was not found",
    "requestId": "REQ-..."
  }
}
```

---

## Troubleshooting

| Problem | Fix |
|---|---|
| Connection refused on 8080 | Run `mvn spring-boot:run` in the terminal |
| {{simId}} shows in URL literally | Run the Create request first (Step 5.1) |
| 400 on valid request | Check Content-Type: application/json header is set |
| Build fails | Ensure Java 17 is installed |

---

*NEXUS-COMPLY Package B (pi2) — Spring Boot 3.1.5 · Java 17 · MongoDB*
