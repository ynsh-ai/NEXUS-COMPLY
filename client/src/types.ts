export type Severity = "Critical" | "High" | "Medium" | "Low";

export type FindingStatus =
  | "OPEN"
  | "ACKNOWLEDGED"
  | "IN_REVIEW"
  | "REMEDIATION_PLANNED"
  | "RESOLVED"
  | "FALSE_POSITIVE";

export type AuditStatus =
  | "COMPLETED"
  | "CHECKING"
  | "RUNNING"
  | "FAILED"
  | "QUEUED"
  | "PARSING"
  | "NORMALIZING";

export type DeviceStatus = "Healthy" | "At risk" | "Critical" | "Unknown";

export interface Device {
  id: string;
  hostname: string;
  vendor: string;
  platform: string;
  ip: string;
  model: string;
  osVersion: string;
  environment: string;
  location: string;
  status: DeviceStatus;
  lastAudit: string;
  risk: number;
  compliance: number;
  criticality: "Critical" | "High" | "Medium" | "Low";
  findings: number;
}

export interface Configuration {
  id: string;
  deviceId: string;
  version: number;
  filename: string;
  uploadedBy: string;
  uploadedAt: string;
  size: string;
  status: string;
  hash: string;
  lines: string[];
}

export interface Audit {
  id: string;
  deviceId: string;
  configurationId: string;
  date: string;
  createdAt: string;
  duration: string;
  findings: number;
  frameworks: string[];
  compliance: number;
  status: AuditStatus;
}

export interface Finding {
  id: string;
  deviceId: string;
  auditId: string;
  control: string;
  title: string;
  severity: Severity;
  status: FindingStatus;
  line: number;
  framework: string;
  createdAt: string;
  rule: string;
  description: string;
  expected: string;
  actual: string;
  impact: string;
  canonicalField: string;
  remediation: string[];
}

export interface Framework {
  id: string;
  name: string;
  version: string;
  category: string;
  mappedRules: number;
  coverage: number;
  controls: number;
  note: string;
}

export interface DriftEvent {
  id: string;
  deviceId: string;
  version: number;
  date: string;
  change: string;
  impact: "Increased" | "Decreased";
  controls: string[];
  riskBefore: number;
  riskAfter: number;
  finding?: string;
}

export interface AiMapping {
  id: string;
  syntax: string;
  vendor: string;
  confidence: number;
  canonicalField: string;
  suggestedValue: string;
  reason: string;
  status: "Needs review" | "Approved" | "Rejected";
  reviewer: string;
  date: string;
}

export interface Report {
  id: string;
  title: string;
  type: string;
  deviceId: string;
  auditId: string;
  date: string;
  status: "Ready" | "Processing" | "Archived";
  compliance: number;
}

export interface DashboardData {
  riskTrend: { month: string; score: number }[];
  complianceTrend: { month: string; score: number }[];
  frameworkScores: { label: string; score: number; name: string }[];
}
