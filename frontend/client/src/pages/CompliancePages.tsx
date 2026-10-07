import { useState } from "react";
import { Link } from "wouter";
import { Icon } from "@/components/Icon";
import {
  BarChartCard,
  Button,
  EmptyState,
  LinkButton,
  Metric,
  Notice,
  Panel,
  ProgressBar,
  RiskLineChart,
  StatusBadge,
} from "@/components/WorkspaceComponents";
import { useAudits, useFindings, useDevices, useConfigurations, useDashboard } from "@/lib/useApi";

export function Audits() {
  const [status, setStatus] = useState("All status");
  const { data: audits } = useAudits();
  const filtered = audits.filter(
    audit => status === "All status" || audit.status === status
  );
  return (
    <div className="page-stack">
      <div className="page-actions">
        <div className="filter-control">
          <Icon name="filter" size={14} />
          <select
            value={status}
            onChange={event => setStatus(event.target.value)}
          >
            <option>All status</option>
            <option>COMPLETED</option>
            <option>CHECKING</option>
            <option>RUNNING</option>
            <option>FAILED</option>
          </select>
        </div>
        <LinkButton href="/audits/new">
          <Icon name="plus" size={15} /> New audit
        </LinkButton>
      </div>
      <div className="audit-progress">
        <div className="audit-progress-copy">
          <div className="eyebrow">Audit pipeline</div>
          <strong>One evidence trail from parse to decision</strong>
          <span>
            Every run keeps vendor detection, normalization, control checks, and
            findings in the same context.
          </span>
        </div>
        <div className="pipeline-steps">
          {["Queued", "Parsing", "Normalizing", "Checking", "Completed"].map(
            (step, index) => (
              <div
                className={`pipeline-step ${index < 4 ? "step-complete" : ""}`}
                key={step}
              >
                <span>
                  {index < 4 ? <Icon name="check" size={12} /> : index + 1}
                </span>
                <small>{step}</small>
              </div>
            )
          )}
        </div>
      </div>
      <Panel
        title="Compliance runs"
        eyebrow={`${filtered.length} recent audits`}
      >
        <div className="table-scroll">
          <div className="data-table audit-table">
            <div className="data-row data-head">
              <span>Audit ID</span>
              <span>Device</span>
              <span>Configuration</span>
              <span>Frameworks</span>
              <span>Compliance</span>
              <span>Status</span>
              <span>Created</span>
              <span>Findings</span>
              <span />
            </div>
            {filtered.map(audit => (
              <Link
                href={`/audits/${audit.id}`}
                className="data-row"
                key={audit.id}
              >
                <span className="mono">
                  <strong>{audit.id}</strong>
                </span>
                <span>
                  {
                    devices.find(device => device.id === audit.deviceId)
                      ?.hostname
                  }
                </span>
                <span>
                  Version{" "}
                  {
                    configurations.find(
                      config => config.id === audit.configurationId
                    )?.version
                  }
                </span>
                <span>{audit.frameworks.join(" · ")}</span>
                <span className="score-cell">{audit.compliance}%</span>
                <span>
                  <StatusBadge value={audit.status} />
                </span>
                <span>{audit.createdAt}</span>
                <span>{audit.findings}</span>
                <span>
                  <Icon name="chevron" size={14} />
                </span>
              </Link>
            ))}
          </div>
        </div>
        {!filtered.length && <EmptyState title="No audits match" />}
      </Panel>
    </div>
  );
}

export function AuditDetail({ id }: { id: string }) {
  const { data: audits } = useAudits();
  const { data: devices } = useDevices();
  const { data: configurations } = useConfigurations();
  const audit = audits.find(item => item.id === id) ?? audits[0];
  const device = devices.find(item => item.id === audit?.deviceId)!;
  const config = configurations.find(
    item => item.id === audit?.configurationId
  )!;
  const { data: allFindings } = useFindings({ auditId: audit?.id });
  const auditFindings = allFindings;
  const [showSuccess, setShowSuccess] = useState(false);
  return (
    <div className="page-stack">
      <div className="detail-header">
        <div>
          <div className="detail-back">
            <Link href="/audits">
              <Icon name="arrow" size={14} className="rotate-180" /> Audits
            </Link>
          </div>
          <div className="eyebrow">{audit.id}</div>
          <h2>{device.hostname} compliance audit</h2>
          <div className="detail-subline">
            <span>Configuration v{config.version}</span>
            <span>•</span>
            <span>{audit.frameworks.join(" · ")}</span>
            <span>•</span>
            <span>{audit.createdAt}</span>
          </div>
        </div>
        <div className="header-actions">
          <StatusBadge value={audit.status} />
          <Button variant="secondary" onClick={() => setShowSuccess(true)}>
            <Icon name="report" size={14} /> Generate report
          </Button>
        </div>
      </div>
      {showSuccess && (
        <Notice>
          Report generation queued. The preview will preserve this audit's
          evidence and scores.
        </Notice>
      )}
      <div className="metric-grid four">
        <Metric
          label="Compliance"
          value={`${audit.compliance}%`}
          note="Weighted control score"
          tone={audit.compliance > 80 ? "good" : "warn"}
        />
        <Metric
          label="Findings"
          value={audit.findings}
          note="Across selected frameworks"
          tone="warn"
        />
        <Metric
          label="Audit duration"
          value={audit.duration}
          note="Parser + rule engine"
        />
        <Metric
          label="Evidence coverage"
          value="88%"
          note="42 of 48 controls mapped"
          tone="good"
        />
      </div>
      <div className="audit-stage">
        <div className="stage-header">
          <div>
            <div className="eyebrow">Run status</div>
            <strong>Pipeline execution</strong>
          </div>
          <span className="stage-time">Last update 09:45:12 IST</span>
        </div>
        <div className="stage-track">
          {["QUEUED", "PARSING", "NORMALIZING", "CHECKING", "COMPLETED"].map(
            (stage, index) => (
              <div
                className={`stage-node ${index < 4 || audit.status === "COMPLETED" ? "done" : index === 4 ? "current" : ""}`}
                key={stage}
              >
                <span>
                  {index < 4 || audit.status === "COMPLETED" ? (
                    <Icon name="check" size={12} />
                  ) : (
                    index + 1
                  )}
                </span>
                <small>{stage}</small>
              </div>
            )
          )}
        </div>
      </div>
      <div className="grid-2-1">
        <Panel
          title="Findings created"
          eyebrow="Deterministic rule results"
          action={
            <Link href="/findings" className="panel-link">
              View all <Icon name="arrow" size={13} />
            </Link>
          }
        >
          <div className="finding-summary-list">
            {auditFindings.length ? (
              auditFindings.map(finding => (
                <Link
                  href={`/findings/${finding.id}`}
                  className="finding-summary-row"
                  key={finding.id}
                >
                  <div>
                    <span className="mono">{finding.id}</span>
                    <strong>{finding.title}</strong>
                  </div>
                  <StatusBadge value={finding.severity} />
                  <Icon name="chevron" size={14} />
                </Link>
              ))
            ) : (
              <EmptyState
                title="No findings created"
                detail="This audit has no finding records."
              />
            )}
          </div>
        </Panel>
        <Panel title="Control outcome" eyebrow="Framework mix">
          <div className="outcome-bars">
            {audit.frameworks.map((framework, index) => (
              <div key={framework}>
                <div>
                  <span>{framework}</span>
                  <strong>{Math.max(0, audit.compliance - index * 5)}%</strong>
                </div>
                <ProgressBar
                  value={Math.max(0, audit.compliance - index * 5)}
                  tone={index === 0 ? "green" : "blue"}
                />
              </div>
            ))}
          </div>
        </Panel>
      </div>
      <Panel title="Audit context" eyebrow="Traceability">
        <div className="detail-list context-list">
          <div>
            <span>Device</span>
            <Link href={`/devices/${device.id}`}>
              <strong>
                {device.hostname} <Icon name="external" size={12} />
              </strong>
            </Link>
          </div>
          <div>
            <span>Configuration</span>
            <Link href={`/configurations/${config.id}`}>
              <strong>
                Version {config.version} <Icon name="external" size={12} />
              </strong>
            </Link>
          </div>
          <div>
            <span>Normalized model</span>
            <Link href={`/configurations/${config.id}/normalized`}>
              <strong>
                Common security model <Icon name="external" size={12} />
              </strong>
            </Link>
          </div>
          <div>
            <span>Framework evidence</span>
            <strong>{audit.frameworks.join(" · ")}</strong>
          </div>
        </div>
      </Panel>
    </div>
  );
}

export function Findings() {
  const [query, setQuery] = useState("");
  const [severity, setSeverity] = useState("All severity");
  const { data: findings } = useFindings();
  const filtered = findings.filter(
    finding =>
      `${finding.id} ${finding.title} ${finding.framework} ${finding.control}`
        .toLowerCase()
        .includes(query.toLowerCase()) &&
      (severity === "All severity" || finding.severity === severity)
  );
  return (
    <div className="page-stack">
      <div className="page-actions">
        <div className="search-control">
          <Icon name="search" size={15} />
          <input
            value={query}
            onChange={event => setQuery(event.target.value)}
            placeholder="Search findings, controls, devices"
          />
        </div>
        <div className="filter-control">
          <Icon name="filter" size={14} />
          <select
            value={severity}
            onChange={event => setSeverity(event.target.value)}
          >
            <option>All severity</option>
            <option>Critical</option>
            <option>High</option>
            <option>Medium</option>
            <option>Low</option>
          </select>
        </div>
      </div>
      <div className="metric-grid four">
        <Metric label="Open" value="23" note="Requires action" tone="warn" />
        <Metric label="Critical" value="4" note="Across 2 assets" tone="bad" />
        <Metric label="In review" value="6" note="Analyst queue" />
        <Metric
          label="Resolved this month"
          value="18"
          note="+4 vs Aug"
          tone="good"
        />
      </div>
      <Panel
        title="Finding register"
        eyebrow={`${filtered.length} visible records`}
      >
        <div className="table-scroll">
          <div className="data-table findings-table">
            <div className="data-row data-head">
              <span>Finding ID / title</span>
              <span>Framework / control</span>
              <span>Severity</span>
              <span>Status</span>
              <span>Device</span>
              <span>Audit</span>
              <span>Created</span>
              <span />
            </div>
            {filtered.map(finding => (
              <Link
                href={`/findings/${finding.id}`}
                className="data-row"
                key={finding.id}
              >
                <span>
                  <strong className="mono">{finding.id}</strong>
                  <small>{finding.title}</small>
                </span>
                <span>
                  <strong>{finding.framework}</strong>
                  <small>{finding.control}</small>
                </span>
                <span>
                  <StatusBadge value={finding.severity} />
                </span>
                <span>
                  <StatusBadge value={finding.status} />
                </span>
                <span>
                  {
                    devices.find(device => device.id === finding.deviceId)
                      ?.hostname
                  }
                </span>
                <span className="mono">{finding.auditId}</span>
                <span>{finding.createdAt}</span>
                <span>
                  <Icon name="chevron" size={14} />
                </span>
              </Link>
            ))}
          </div>
        </div>
        {!filtered.length && (
          <EmptyState
            title="No findings match"
            detail="Try a different search or severity filter."
          />
        )}
      </Panel>
    </div>
  );
}

export function FindingDetail({
  id,
  evidenceOnly = false,
}: {
  id: string;
  evidenceOnly?: boolean;
}) {
  const { data: findings } = useFindings();
  const { data: devices } = useDevices();
  const { data: configurations } = useConfigurations();
  const finding = findings.find(item => item.id === id) ?? findings[0];
  const device = devices.find(item => item.id === finding?.deviceId)!;
  const config = configurations.find(
    item => item.deviceId === finding?.deviceId
  )!;
  const [activeTab, setActiveTab] = useState(
    evidenceOnly ? "Evidence" : "Evidence"
  );
  const [status, setStatus] = useState(finding.status);
  const [saved, setSaved] = useState(false);
  return (
    <div className="page-stack">
      <div className="detail-header">
        <div>
          <div className="detail-back">
            <Link href="/findings">
              <Icon name="arrow" size={14} className="rotate-180" /> Findings
            </Link>
          </div>
          <div className="eyebrow">
            {finding.id} · {finding.framework} / {finding.control}
          </div>
          <h2>{finding.title}</h2>
          <div className="detail-subline">
            <Link href={`/devices/${device.id}`}>{device.hostname}</Link>
            <span>•</span>
            <span>Detected {finding.createdAt}</span>
            <span>•</span>
            <span>{finding.rule}</span>
          </div>
        </div>
        <div className="header-actions">
          <StatusBadge value={finding.severity} />
          <select
            className="inline-select"
            value={status}
            onChange={event =>
              setStatus(event.target.value as typeof finding.status)
            }
          >
            <option>OPEN</option>
            <option>ACKNOWLEDGED</option>
            <option>IN_REVIEW</option>
            <option>REMEDIATION_PLANNED</option>
            <option>RESOLVED</option>
            <option>FALSE_POSITIVE</option>
          </select>
          <Button onClick={() => setSaved(true)}>Save status</Button>
        </div>
      </div>
      {saved && (
        <Notice>
          Finding status updated to {formatStatus(status)}. Backend
          authorization remains authoritative.
        </Notice>
      )}
      <div className="finding-explainer">
        <div className="explainer-column">
          <span className="eyebrow">What failed?</span>
          <p>{finding.description}</p>
        </div>
        <div className="explainer-column">
          <span className="eyebrow">Expected state</span>
          <div className="state-box state-good">{finding.expected}</div>
        </div>
        <div className="explainer-column">
          <span className="eyebrow">Actual state</span>
          <div className="state-box state-bad">{finding.actual}</div>
        </div>
      </div>
      <div className="grid-2-1">
        <Panel title="Finding context" eyebrow="Why this matters">
          <div className="detail-list">
            <div>
              <span>Impact</span>
              <strong>{finding.impact}</strong>
            </div>
            <div>
              <span>Framework / control</span>
              <Link href={`/controls/${finding.control}`}>
                <strong>
                  {finding.framework} · {finding.control}{" "}
                  <Icon name="external" size={12} />
                </strong>
              </Link>
            </div>
            <div>
              <span>Device criticality</span>
              <strong>
                {device.criticality} · {device.environment}
              </strong>
            </div>
            <div>
              <span>Canonical field</span>
              <strong className="mono">{finding.canonicalField}</strong>
            </div>
          </div>
        </Panel>
        <Panel title="Risk factors" eyebrow="Explainable prioritization">
          <div className="factor-list">
            <div>
              <span>Finding severity</span>
              <strong className="risk-high">{finding.severity}</strong>
            </div>
            <div>
              <span>Asset criticality</span>
              <strong>{device.criticality}</strong>
            </div>
            <div>
              <span>Exposure surface</span>
              <strong>Management plane</strong>
            </div>
            <div>
              <span>Compliance impact</span>
              <strong>{finding.framework}</strong>
            </div>
          </div>
        </Panel>
      </div>
      <div className="tab-strip detail-tabs">
        {["Evidence", "Remediation", "Mapping"].map(tab => (
          <button
            key={tab}
            className={activeTab === tab ? "tab-active" : ""}
            onClick={() => setActiveTab(tab)}
          >
            {tab}
          </button>
        ))}
      </div>
      {activeTab === "Evidence" && (
        <Panel title="Configuration evidence" eyebrow="Exact source trace">
          <div className="evidence-header">
            <div>
              <span>Configuration version</span>
              <strong>
                v{config.version} · {config.filename}
              </strong>
            </div>
            <div>
              <span>Line</span>
              <strong className="mono">{finding.line}</strong>
            </div>
            <div>
              <span>Control</span>
              <strong>{finding.control}</strong>
            </div>
            <LinkButton href={`/configurations/${config.id}`}>
              Open configuration <Icon name="external" size={13} />
            </LinkButton>
          </div>
          <div className="code-viewer evidence-viewer">
            {config.lines
              .slice(
                Math.max(0, finding.line - 3),
                Math.min(config.lines.length, finding.line + 3)
              )
              .map((line, index) => {
                const number = Math.max(1, finding.line - 2) + index;
                return (
                  <div
                    className={`code-line ${number === finding.line ? "code-highlight" : ""}`}
                    key={number}
                  >
                    <span className="line-number">
                      {String(number).padStart(2, "0")}
                    </span>
                    <code>{line}</code>
                  </div>
                );
              })}
          </div>
          <div className="evidence-state-grid">
            <div>
              <span>Expected state</span>
              <strong>{finding.expected}</strong>
            </div>
            <div>
              <span>Actual state</span>
              <strong>{finding.actual}</strong>
            </div>
          </div>
        </Panel>
      )}
      {activeTab === "Remediation" && (
        <Panel
          title="Recommended remediation"
          eyebrow={`${device.vendor} · ${device.platform}`}
        >
          <div className="remediation-layout">
            <div className="remediation-copy">
              <p>{finding.impact}</p>
              <ol>
                {finding.remediation.map((step, index) => (
                  <li key={step}>
                    <span>{index + 1}</span>
                    {step}
                  </li>
                ))}
              </ol>
            </div>
            <div className="validation-box">
              <span className="eyebrow">Validation</span>
              <strong>Re-run the affected control after deployment.</strong>
              <span>
                Remediation does not modify the configuration history in this
                frontend.
              </span>
              <Button variant="secondary" onClick={() => setSaved(true)}>
                Mark remediation planned
              </Button>
            </div>
          </div>
        </Panel>
      )}
      {activeTab === "Mapping" && (
        <Panel title="Rule mapping" eyebrow="Deterministic compliance input">
          <div className="detail-list">
            <div>
              <span>Framework</span>
              <strong>{finding.framework}</strong>
            </div>
            <div>
              <span>Control</span>
              <strong>{finding.control}</strong>
            </div>
            <div>
              <span>Rule</span>
              <strong className="mono">{finding.rule}</strong>
            </div>
            <div>
              <span>Canonical field</span>
              <strong className="mono">{finding.canonicalField}</strong>
            </div>
            <div>
              <span>Decision authority</span>
              <strong>Deterministic rule engine</strong>
            </div>
          </div>
        </Panel>
      )}
    </div>
  );
}

function formatStatus(status: string) {
  return status
    .replaceAll("_", " ")
    .toLowerCase()
    .replace(/(^| )\S/g, letter => letter.toUpperCase());
}

export function RiskCenter() {
  const { data: devices } = useDevices();
  const { data: dashboard } = useDashboard();
  const { data: findings } = useFindings();
  const riskByDevice = devices.map(device => ({
    name: device.hostname.replace("-", " "),
    count: device.risk,
  }));
  const riskByCategory = [
    { name: "Access", count: 62 },
    { name: "Logging", count: 54 },
    { name: "Services", count: 48 },
    { name: "Crypto", count: 34 },
    { name: "Network", count: 29 },
  ];
  return (
    <div className="page-stack">
      <div className="page-actions">
        <div className="date-filter">
          <Icon name="clock" size={14} /> Portfolio view · Sep 23, 2026
        </div>
        <LinkButton href="/findings" variant="secondary">
          Review findings <Icon name="arrow" size={14} />
        </LinkButton>
      </div>
      <div className="metric-grid four">
        <Metric
          label="Overall risk score"
          value="46"
          note="↓ 11 pts since Apr"
          tone="warn"
        />
        <Metric
          label="Critical assets"
          value="2"
          note="EDGE-RTR-01 · PA-DC-01"
          tone="bad"
        />
        <Metric
          label="High-risk devices"
          value="2"
          note="Score above 60"
          tone="warn"
        />
        <Metric
          label="Open high / critical"
          value="12"
          note="4 critical · 8 high"
          tone="bad"
        />
      </div>
      <div className="grid-1-1">
        <Panel title="Risk trend" eyebrow="Portfolio score · lower is better">
          <RiskLineChart data={dashboard.riskTrend} />
        </Panel>
        <Panel title="Risk by category" eyebrow="Weighted exposure">
          <BarChartCard data={riskByCategory} />
        </Panel>
      </div>
      <div className="grid-1-1">
        <Panel title="Risk by device" eyebrow="Current device posture">
          <div className="risk-device-list">
            {riskByDevice.map(item => (
              <div className="risk-device" key={item.name}>
                <div>
                  <strong>{item.name}</strong>
                  <span>Weighted score</span>
                </div>
                <ProgressBar
                  value={item.count}
                  tone={
                    item.count > 65
                      ? "red"
                      : item.count > 45
                        ? "amber"
                        : "green"
                  }
                />
                <strong className={item.count > 65 ? "risk-high" : ""}>
                  {item.count}
                </strong>
              </div>
            ))}
          </div>
        </Panel>
        <Panel
          title="Severity vs asset criticality"
          eyebrow="Prioritization matrix"
        >
          <div className="risk-matrix">
            <div className="matrix-label y">Finding severity</div>
            <div className="matrix-grid">
              {[
                ["low", "low", "medium"],
                ["low", "medium", "high"],
                ["medium", "high", "critical"],
              ]
                .flat()
                .map((cell, index) => (
                  <div
                    className={`matrix-cell ${cell}`}
                    key={`${cell}-${index}`}
                  >
                    <span>{index % 3 === 2 ? "" : index === 7 ? "2" : ""}</span>
                  </div>
                ))}
            </div>
            <div className="matrix-axis">
              <span>Medium</span>
              <span>High</span>
              <span>Critical</span>
            </div>
            <div className="matrix-label x">Asset criticality</div>
          </div>
        </Panel>
      </div>
      <Panel title="Prioritized risk register" eyebrow="Explainable factors">
        <div className="table-scroll">
          <div className="data-table risk-table">
            <div className="data-row data-head">
              <span>Finding</span>
              <span>Device</span>
              <span>Severity</span>
              <span>Asset criticality</span>
              <span>Exposure</span>
              <span>Risk score</span>
              <span>Risk factors</span>
              <span>Status</span>
            </div>
            {findings.slice(0, 4).map(finding => {
              const device = devices.find(
                item => item.id === finding.deviceId
              );
              const score = Math.min(
                96,
                (device?.risk ?? 0) +
                  (finding.severity === "Critical"
                    ? 18
                    : finding.severity === "High"
                      ? 10
                      : 2)
              );
              return (
                <Link
                  href={`/findings/${finding.id}`}
                  className="data-row"
                  key={finding.id}
                >
                  <span>
                    <strong>{finding.id}</strong>
                    <small>{finding.title}</small>
                  </span>
                  <span>{device?.hostname}</span>
                  <span>
                    <StatusBadge value={finding.severity} />
                  </span>
                  <span>{device?.criticality}</span>
                  <span>Management plane</span>
                  <span className="risk-high">{score}</span>
                  <span>Severity · criticality · {finding.framework}</span>
                  <span>
                    <StatusBadge value={finding.status} />
                  </span>
                </Link>
              );
            })}
          </div>
        </div>
      </Panel>
    </div>
  );
}
