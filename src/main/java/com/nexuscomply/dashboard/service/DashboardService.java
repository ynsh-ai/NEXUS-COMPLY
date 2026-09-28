package com.nexuscomply.dashboard.service;

import com.nexuscomply.drift.repository.DriftEventRepository;
import com.nexuscomply.report.repository.ReportRepository;
import com.nexuscomply.risk.repository.RiskAssessmentRepository;
import com.nexuscomply.simulation.repository.SimulationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * BUG-002, BUG-012, BUG-016 fixed: DashboardService now injects real repositories
 * and returns meaningful aggregate data.
 */
@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final RiskAssessmentRepository riskRepo;
    private final DriftEventRepository driftRepo;
    private final ReportRepository reportRepo;
    private final SimulationRepository simulationRepo;

    public DashboardService(RiskAssessmentRepository riskRepo,
                            DriftEventRepository driftRepo,
                            ReportRepository reportRepo,
                            SimulationRepository simulationRepo) {
        this.riskRepo = riskRepo;
        this.driftRepo = driftRepo;
        this.reportRepo = reportRepo;
        this.simulationRepo = simulationRepo;
    }

    public Map<String, Object> getSummary() {
        return Map.of(
                "totalRiskAssessments", riskRepo.count(),
                "totalDriftEvents",     driftRepo.count(),
                "totalReports",         reportRepo.count(),
                "totalSimulations",     simulationRepo.count()
        );
    }

    public Map<String, Object> getCompliance() {
        // Compliance data is owned by Cyber Engine / Package A — returns placeholder.
        return Map.of("message", "Compliance data provided by Cyber Engine", "data", Map.of());
    }

    public Map<String, Object> getFindings() {
        // Findings owned by Package A — returns placeholder.
        return Map.of("message", "Findings data provided by Package A", "data", Map.of());
    }

    public Map<String, Object> getRisk() {
        return Map.of(
                "totalRisks", riskRepo.count()
        );
    }

    public Map<String, Object> getDrift() {
        return Map.of(
                "totalDriftEvents", driftRepo.count()
        );
    }

    public Map<String, Object> getActivity() {
        return Map.of(
                "recentReports",      reportRepo.count(),
                "recentSimulations",  simulationRepo.count()
        );
    }

    public Map<String, Object> getFrameworks() {
        // Framework data is owned by Package A — returns placeholder.
        return Map.of("message", "Framework data provided by Package A", "data", Map.of());
    }
}
