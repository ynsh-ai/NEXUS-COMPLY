import { Route, Switch, Redirect } from "wouter";
import ErrorBoundary from "@/components/ErrorBoundary";
import { AuthProvider, useAuth } from "@/contexts/AuthContext";
import { AppShell } from "@/layout/AppShell";
import Login from "@/pages/Login";
import {
  Dashboard,
  DeviceDetails,
  DeviceForm,
  Devices,
  Configurations,
  ConfigDetail,
  NormalizedConfig,
} from "@/pages/Workspace";
import {
  Audits,
  AuditDetail,
  Findings,
  FindingDetail,
  RiskCenter,
} from "@/pages/CompliancePages";
import {
  AiAnalyst,
  ControlDetail,
  Drift,
  FrameworkDetail,
  Frameworks,
  ReportPreview,
  Reports,
  Settings,
  WhatIf,
} from "@/pages/IntelligencePages";

function Protected({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuth();
  if (!isAuthenticated) {
    return <Redirect to="/login" />;
  }
  return <AppShell>{children}</AppShell>;
}

function Router() {
  return (
    <Switch>
      <Route path="/login" component={Login} />
      <Route path="/">
        {() => <Redirect to="/login" />}
      </Route>
      <Route path="/dashboard">
        {() => (
          <Protected>
            <Dashboard />
          </Protected>
        )}
      </Route>
      <Route path="/devices/new">
        {() => (
          <Protected>
            <DeviceForm />
          </Protected>
        )}
      </Route>
      <Route path="/devices/:id/edit">
        {params => (
          <Protected>
            <DeviceForm edit={params.id === "dev-001"} />
          </Protected>
        )}
      </Route>
      <Route path="/devices/:id">
        {params => (
          <Protected>
            <DeviceDetails id={params.id} />
          </Protected>
        )}
      </Route>
      <Route path="/devices">
        {() => (
          <Protected>
            <Devices />
          </Protected>
        )}
      </Route>
      <Route path="/configurations/:id/normalized">
        {params => (
          <Protected>
            <NormalizedConfig id={params.id} />
          </Protected>
        )}
      </Route>
      <Route path="/configurations/:id">
        {params => (
          <Protected>
            <ConfigDetail id={params.id} />
          </Protected>
        )}
      </Route>
      <Route path="/configurations">
        {() => (
          <Protected>
            <Configurations />
          </Protected>
        )}
      </Route>
      <Route path="/audits/new">
        {() => (
          <Protected>
            <NewAudit />
          </Protected>
        )}
      </Route>
      <Route path="/audits/:id">
        {params => (
          <Protected>
            <AuditDetail id={params.id} />
          </Protected>
        )}
      </Route>
      <Route path="/audits">
        {() => (
          <Protected>
            <Audits />
          </Protected>
        )}
      </Route>
      <Route path="/findings/:id/evidence">
        {params => (
          <Protected>
            <FindingDetail id={params.id} evidenceOnly />
          </Protected>
        )}
      </Route>
      <Route path="/findings/:id">
        {params => (
          <Protected>
            <FindingDetail id={params.id} />
          </Protected>
        )}
      </Route>
      <Route path="/findings">
        {() => (
          <Protected>
            <Findings />
          </Protected>
        )}
      </Route>
      <Route path="/risk">
        {() => (
          <Protected>
            <RiskCenter />
          </Protected>
        )}
      </Route>
      <Route path="/drift">
        {() => (
          <Protected>
            <Drift />
          </Protected>
        )}
      </Route>
      <Route path="/what-if">
        {() => (
          <Protected>
            <WhatIf />
          </Protected>
        )}
      </Route>
      <Route path="/ai-analyst">
        {() => (
          <Protected>
            <AiAnalyst />
          </Protected>
        )}
      </Route>
      <Route path="/reports/:id">
        {params => (
          <Protected>
            <ReportPreview id={params.id} />
          </Protected>
        )}
      </Route>
      <Route path="/reports">
        {() => (
          <Protected>
            <Reports />
          </Protected>
        )}
      </Route>
      <Route path="/frameworks/:id">
        {params => (
          <Protected>
            <FrameworkDetail id={params.id} />
          </Protected>
        )}
      </Route>
      <Route path="/frameworks">
        {() => (
          <Protected>
            <Frameworks />
          </Protected>
        )}
      </Route>
      <Route path="/controls/:id">
        {params => (
          <Protected>
            <ControlDetail id={params.id} />
          </Protected>
        )}
      </Route>
      <Route path="/settings">
        {() => (
          <Protected>
            <Settings />
          </Protected>
        )}
      </Route>
      <Route>
        {() => (
          <Protected>
            <Dashboard />
          </Protected>
        )}
      </Route>
    </Switch>
  );
}

function NewAudit() {
  return (
    <div className="page-stack narrow-page">
      <div className="detail-back">
        <a href="/audits">← Back to audits</a>
      </div>
      <div className="detail-header">
        <div>
          <div className="eyebrow">Create compliance run</div>
          <h2>New audit</h2>
          <div className="detail-subline">
            <span>
              Select context, review scope, and start the deterministic rule
              engine.
            </span>
          </div>
        </div>
      </div>
      <div className="audit-wizard">
        <div className="wizard-steps">
          <div className="wizard-step active">
            <span>1</span>
            <strong>Context</strong>
            <small>Device and version</small>
          </div>
          <div className="wizard-step">
            <span>2</span>
            <strong>Frameworks</strong>
            <small>Choose coverage</small>
          </div>
          <div className="wizard-step">
            <span>3</span>
            <strong>Review</strong>
            <small>Start audit</small>
          </div>
        </div>
        <div className="form-grid">
          <label>
            Device
            <select defaultValue="dev-001">
              <option>EDGE-RTR-01</option>
              <option>FORTI-GW-02</option>
              <option>PA-DC-01</option>
            </select>
          </label>
          <label>
            Configuration version
            <select defaultValue="Version 14">
              <option>Version 14 · Sep 23, 2026</option>
              <option>Version 13 · Sep 16, 2026</option>
            </select>
          </label>
          <label className="span-2">
            Frameworks
            <select defaultValue="CIS + NIST SP 800-53">
              <option>CIS + NIST SP 800-53</option>
              <option>All configured frameworks</option>
              <option>CIS only</option>
            </select>
          </label>
          <div className="form-actions span-2">
            <a className="btn btn-ghost" href="/audits">
              Cancel
            </a>
            <a className="btn btn-primary" href="/audits/AUD-2026-0918">
              Review and start <span>→</span>
            </a>
          </div>
        </div>
      </div>
    </div>
  );
}

export default function App() {
  return (
    <ErrorBoundary>
      <AuthProvider>
        <Router />
      </AuthProvider>
    </ErrorBoundary>
  );
}
