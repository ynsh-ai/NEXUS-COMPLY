/**
 * NEXUS-COMPLY Data Hooks
 *
 * React hooks that fetch live data from the backend.
 * On network failure they silently fall back to the local mock dataset,
 * keeping the UI functional during development without the backend running.
 *
 * Pattern:
 *   const { data, loading, error } = useAudits();
 */

import { useEffect, useState } from "react";
import { api } from "./api";
import type {
  Audit,
  Configuration,
  DashboardData,
  Device,
  DriftEvent,
  Finding,
  Framework,
} from "@/types";

// ---------- helpers ---------------------------------------------------------

function useApiData<T>(
  fetcher: () => Promise<T>,
  fallback: T,
  deps: unknown[] = []
): { data: T; loading: boolean; error: string | null } {
  const [data, setData] = useState<T>(fallback);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(
    () => {
      let cancelled = false;
      setLoading(true);
      fetcher()
        .then(result => {
          if (!cancelled) {
            // If backend returned an empty array / null, keep fallback
            if (
              result === null ||
              result === undefined ||
              (Array.isArray(result) && result.length === 0)
            ) {
              setData(fallback);
            } else {
              setData(result as T);
            }
            setError(null);
          }
        })
        .catch((err: unknown) => {
          if (!cancelled) {
            console.warn("[useApiData] falling back to mock data:", err);
            setData(fallback);
            setError(err instanceof Error ? err.message : String(err));
          }
        })
        .finally(() => {
          if (!cancelled) setLoading(false);
        });
      return () => {
        cancelled = true;
      };
    },
    // eslint-disable-next-line react-hooks/exhaustive-deps
    deps
  );

  return { data, loading, error };
}

// ---------- public hooks ----------------------------------------------------

export function useAudits() {
  return useApiData<Audit[]>(
    () => api.audits.list() as Promise<Audit[]>,
    []
  );
}

export function useAudit(id: string) {
  return useApiData<Audit | undefined>(
    () => api.audits.get(id) as Promise<Audit>,
    undefined,
    [id]
  );
}

export function useFindings(params?: {
  auditId?: string;
  severity?: string;
  status?: string;
  deviceId?: string;
}) {
  const key = JSON.stringify(params ?? {});
  return useApiData<Finding[]>(
    () => api.findings.list(params) as Promise<Finding[]>,
    [],
    [key]
  );
}

export function useFinding(id: string) {
  return useApiData<Finding | undefined>(
    () => api.findings.get(id) as Promise<Finding>,
    undefined,
    [id]
  );
}

export function useDevices() {
  return useApiData<Device[]>(
    () => api.devices.list() as Promise<Device[]>,
    []
  );
}

export function useDevice(id: string) {
  return useApiData<Device | undefined>(
    () => api.devices.get(id) as Promise<Device>,
    undefined,
    [id]
  );
}

export function useConfigurations(deviceId?: string) {
  return useApiData<Configuration[]>(
    () => api.configurations.list(deviceId) as Promise<Configuration[]>,
    [],
    [deviceId]
  );
}


export function useDashboard() {
  // Merge several dashboard endpoints into one object matching DashboardData
  const [data, setData] = useState<DashboardData>({
    riskTrend: [],
    complianceTrend: [],
    frameworkScores: []
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);

    Promise.all([
      api.dashboard.compliance(),
      api.dashboard.risk(),
      api.dashboard.frameworks(),
    ])
      .then(([compliance, risk, frameworks]) => {
        if (!cancelled) {
          // Map backend aggregate shape to DashboardData type (best-effort)
          const riskTrend = (risk as Record<string, unknown>)?.riskTrend ?? [];
          const complianceTrend = (compliance as Record<string, unknown>)?.complianceTrend ?? [];
          const frameworkScores = (frameworks as Record<string, unknown>)?.frameworkScores ?? [];
          setData({
            riskTrend: riskTrend as DashboardData["riskTrend"],
            complianceTrend:
              complianceTrend as DashboardData["complianceTrend"],
            frameworkScores:
              frameworkScores as DashboardData["frameworkScores"],
          });
          setError(null);
        }
      })
      .catch((err: unknown) => {
        if (!cancelled) {
          console.warn("[useDashboard] fetch failed:", err);
          setError(err instanceof Error ? err.message : String(err));
        }
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });

    return () => {
      cancelled = true;
    };
  }, []);

  return { data, loading, error };
}

export function useFrameworks() {
  return useApiData<Framework[]>(
    () => api.frameworks.list() as Promise<Framework[]>,
    []
  );
}

export function useDriftEvents(deviceId?: string) {
  const key = deviceId ?? "all";
  return useApiData<DriftEvent[]>(
    async () => {
      const result = (await api.drift.list()) as {
        content?: DriftEvent[];
        items?: DriftEvent[];
      };
      const items = result?.content ?? result?.items ?? [];
      return deviceId ? items.filter(e => e.deviceId === deviceId) : items;
    },
    [],
    [key]
  );
}

