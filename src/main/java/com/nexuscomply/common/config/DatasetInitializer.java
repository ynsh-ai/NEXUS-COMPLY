package com.nexuscomply.common.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexuscomply.audit.model.Audit;
import com.nexuscomply.audit.model.AuditScope;
import com.nexuscomply.audit.model.AuditSummary;
import com.nexuscomply.audit.repository.AuditRepository;
import com.nexuscomply.configuration.model.Configuration;
import com.nexuscomply.configuration.repository.ConfigurationRepository;
import com.nexuscomply.device.model.Device;
import com.nexuscomply.device.repository.DeviceRepository;
import com.nexuscomply.drift.model.DriftEvent;
import com.nexuscomply.drift.repository.DriftEventRepository;
import com.nexuscomply.finding.model.Finding;
import com.nexuscomply.finding.repository.FindingRepository;
import com.nexuscomply.framework.model.*;
import com.nexuscomply.framework.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class DatasetInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatasetInitializer.class);

    private final FrameworkRepository frameworkRepo;
    private final ControlRepository controlRepo;
    private final ComplianceRuleRepository complianceRuleRepo;
    private final FrameworkMappingRepository mappingRepo;
    private final VendorKnowledgeRepository vendorKnowledgeRepo;
    private final EvaluationCaseRepository evaluationCaseRepo;
    private final DeviceRepository deviceRepo;
    private final ConfigurationRepository configRepo;
    private final AuditRepository auditRepo;
    private final FindingRepository findingRepo;
    private final DriftEventRepository driftEventRepo;
    private final MongoTemplate mongoTemplate;
    private final ObjectMapper objectMapper;

    public DatasetInitializer(FrameworkRepository frameworkRepo,
                              ControlRepository controlRepo,
                              ComplianceRuleRepository complianceRuleRepo,
                              FrameworkMappingRepository mappingRepo,
                              VendorKnowledgeRepository vendorKnowledgeRepo,
                              EvaluationCaseRepository evaluationCaseRepo,
                              DeviceRepository deviceRepo,
                              ConfigurationRepository configRepo,
                              AuditRepository auditRepo,
                              FindingRepository findingRepo,
                              DriftEventRepository driftEventRepo,
                              MongoTemplate mongoTemplate,
                              ObjectMapper objectMapper) {
        this.frameworkRepo = frameworkRepo;
        this.controlRepo = controlRepo;
        this.complianceRuleRepo = complianceRuleRepo;
        this.mappingRepo = mappingRepo;
        this.vendorKnowledgeRepo = vendorKnowledgeRepo;
        this.evaluationCaseRepo = evaluationCaseRepo;
        this.deviceRepo = deviceRepo;
        this.configRepo = configRepo;
        this.auditRepo = auditRepo;
        this.findingRepo = findingRepo;
        this.driftEventRepo = driftEventRepo;
        this.mongoTemplate = mongoTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) {
        try {
            ensureIndexes();
            seedFrameworks();
            seedVendorKnowledge();
            seedComplianceRules();
            seedEvaluationCases();
            seedBaselineControls();
            seedDevices();
            seedConfigurations();
            seedAudits();
            seedFindings();
            seedDriftEvents();
        } catch (Exception e) {
            log.error("Error during dataset initialization: {}", e.getMessage(), e);
        }
    }

    private void ensureIndexes() {
        try {
            mongoTemplate.indexOps("vendor_knowledge")
                    .ensureIndex(new Index().on("vendor", Sort.Direction.ASC).on("platform", Sort.Direction.ASC));
            mongoTemplate.indexOps("compliance_rules")
                    .ensureIndex(new Index().on("canonicalField", Sort.Direction.ASC).on("status", Sort.Direction.ASC));
            mongoTemplate.indexOps("audits")
                    .ensureIndex(new Index().on("startedAt", Sort.Direction.DESC));
            mongoTemplate.indexOps("findings")
                    .ensureIndex(new Index().on("auditId", Sort.Direction.ASC).on("deviceId", Sort.Direction.ASC));
            mongoTemplate.indexOps("evidence")
                    .ensureIndex(new Index().on("findingId", Sort.Direction.ASC));
            mongoTemplate.indexOps("normalized_configurations")
                    .ensureIndex(new Index().on("configurationId", Sort.Direction.ASC).on("versionId", Sort.Direction.ASC));
            mongoTemplate.indexOps("what_if_simulations")
                    .ensureIndex(new Index().on("createdAt", Sort.Direction.DESC));
            mongoTemplate.indexOps("drift_events")
                    .ensureIndex(new Index().on("deviceId", Sort.Direction.ASC).on("detectedAt", Sort.Direction.DESC));
            log.info("MongoDB collections and indexes successfully initialized.");
        } catch (Exception e) {
            log.warn("Could not create all MongoDB indexes: {}", e.getMessage());
        }
    }

    private void seedFrameworks() {
        if (frameworkRepo.count() == 0) {
            try {
                ClassPathResource resource = new ClassPathResource("seed/mongo_seed/frameworks.json");
                if (resource.exists()) {
                    try (InputStream is = resource.getInputStream()) {
                        List<Framework> frameworks = objectMapper.readValue(is, new TypeReference<List<Framework>>() {});
                        frameworkRepo.saveAll(frameworks);
                        log.info("Seeded {} frameworks from reference dataset.", frameworks.size());
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to seed frameworks: {}", e.getMessage());
            }
        }
    }

    private void seedVendorKnowledge() {
        if (vendorKnowledgeRepo.count() == 0) {
            try {
                ClassPathResource resource = new ClassPathResource("seed/mongo_seed/vendor_knowledge.json");
                if (resource.exists()) {
                    try (InputStream is = resource.getInputStream()) {
                        List<VendorKnowledge> items = objectMapper.readValue(is, new TypeReference<List<VendorKnowledge>>() {});
                        vendorKnowledgeRepo.saveAll(items);
                        log.info("Seeded {} vendor knowledge entries from reference dataset.", items.size());
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to seed vendor knowledge: {}", e.getMessage());
            }
        }
    }

    private void seedComplianceRules() {
        if (complianceRuleRepo.count() == 0) {
            try {
                ClassPathResource resource = new ClassPathResource("seed/mongo_seed/compliance_rules.json");
                if (resource.exists()) {
                    try (InputStream is = resource.getInputStream()) {
                        List<ComplianceRule> rules = objectMapper.readValue(is, new TypeReference<List<ComplianceRule>>() {});
                        complianceRuleRepo.saveAll(rules);
                        log.info("Seeded {} compliance rules from reference dataset.", rules.size());
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to seed compliance rules: {}", e.getMessage());
            }
        }
    }

    private void seedEvaluationCases() {
        if (evaluationCaseRepo.count() == 0) {
            try {
                ClassPathResource resource = new ClassPathResource("seed/mongo_seed/evaluation_cases.json");
                if (resource.exists()) {
                    try (InputStream is = resource.getInputStream()) {
                        List<EvaluationCase> cases = objectMapper.readValue(is, new TypeReference<List<EvaluationCase>>() {});
                        evaluationCaseRepo.saveAll(cases);
                        log.info("Seeded {} evaluation cases from reference dataset.", cases.size());
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to seed evaluation cases: {}", e.getMessage());
            }
        }
    }

    private void seedBaselineControls() {
        if (controlRepo.count() == 0) {
            // Seed authoritative baseline controls linked to frameworks and rules
            Control c1 = new Control(
                    "cis-1-1-1",
                    "FW-CIS",
                    "1.1.1",
                    "Ensure SSH Version 2 is enabled",
                    "Disables unencrypted remote administration protocols like Telnet.",
                    "HIGH",
                    "ACCESS_CONTROL",
                    "Configure 'transport input ssh' and 'ip ssh version 2'.",
                    List.of("RULE-MGMT-SSH-V2")
            );
            Control c2 = new Control(
                    "cis-1-1-2",
                    "FW-CIS",
                    "1.1.2",
                    "Ensure AAA subsystem is active",
                    "Enables centralized AAA access control for authentication.",
                    "HIGH",
                    "AUTHENTICATION",
                    "Configure 'aaa new-model' in global configuration.",
                    List.of("RULE-AUTH-AAA")
            );
            Control c3 = new Control(
                    "cis-1-1-3",
                    "FW-CIS",
                    "1.1.3",
                    "Ensure Telnet service is disabled",
                    "Prohibits plaintext remote access across network interfaces.",
                    "HIGH",
                    "ACCESS_CONTROL",
                    "Disable telnet under line vty / line aux.",
                    List.of("RULE-MGMT-TELNET-OFF")
            );
            Control c4 = new Control(
                    "nist-ac-17",
                    "FW-NIST-800-53",
                    "AC-17",
                    "Remote Access Management",
                    "Authorize and monitor remote access connections using cryptographic mechanisms.",
                    "HIGH",
                    "ACCESS_CONTROL",
                    "Enforce SSH v2 and terminate unencrypted remote channels.",
                    List.of("RULE-MGMT-SSH-V2")
            );
            controlRepo.saveAll(List.of(c1, c2, c3, c4));
            log.info("Seeded baseline controls linked to frameworks and rules.");
        }
    }

    private void seedDevices() {
        if (deviceRepo.count() == 0) {
            Device d1 = new Device("dev-001", "edge-router-01", "Cisco", "IOS-XE", "10.0.1.1",
                    "Cisco Catalyst 8300", "17.06.01a", "Production", "US-East-1",
                    "At risk", "2026-03-28", 48, 82, "Critical", 3);
            Device d2 = new Device("dev-002", "core-switch-01", "Cisco", "IOS-XE", "10.0.1.2",
                    "Cisco Catalyst 9300", "17.09.02", "Production", "US-East-1",
                    "Healthy", "2026-03-25", 15, 96, "High", 1);
            Device d3 = new Device("dev-003", "dist-switch-02", "Juniper", "JunOS", "10.0.2.1",
                    "EX4300", "21.4R1", "Staging", "EU-West-1",
                    "Healthy", "2026-03-20", 20, 94, "Medium", 2);
            Device d4 = new Device("dev-004", "sec-firewall-01", "Palo Alto", "PAN-OS", "10.0.0.1",
                    "PA-3220", "10.2.3", "Production", "US-West-2",
                    "At risk", "2026-03-27", 62, 75, "Critical", 7);
            deviceRepo.saveAll(List.of(d1, d2, d3, d4));
            log.info("Seeded 4 baseline devices into database.");
        }
    }

    private void seedConfigurations() {
        if (configRepo.count() == 0) {
            Configuration c1 = new Configuration(
                    "cfg-001",
                    "dev-001",
                    1,
                    "edge-router-01.cfg",
                    "sec-admin",
                    "2026-03-28 10:15:00",
                    "42 KB",
                    "Active",
                    "sha256-e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                    List.of(
                            "! Current configuration - edge-router-01",
                            "version 17.6",
                            "service timestamps debug datetime msec",
                            "service timestamps log datetime msec",
                            "no service password-encryption",
                            "hostname edge-router-01",
                            "boot-start-marker",
                            "boot-end-marker",
                            "no aaa new-model",
                            "ip domain name corp.nexus.local",
                            "crypto key generate rsa modulus 2048",
                            "ip ssh version 1",
                            "interface GigabitEthernet0/0/0",
                            " description Uplink to Provider",
                            " ip address 10.0.1.1 255.255.255.0",
                            " negotiation auto",
                            " no shutdown",
                            "line con 0",
                            " stopbits 2",
                            "line vty 0 4",
                            " transport input telnet ssh",
                            " login local",
                            "end"
                    )
            );

            Configuration c2 = new Configuration(
                    "cfg-002",
                    "dev-002",
                    1,
                    "core-switch-01.cfg",
                    "net-ops",
                    "2026-03-25 09:30:00",
                    "58 KB",
                    "Active",
                    "sha256-872f3e445b2b98fc1c149afbf4c8996fb92427ae41e4649b934ca495991b11a9",
                    List.of(
                            "! Current configuration - core-switch-01",
                            "version 17.9",
                            "hostname core-switch-01",
                            "aaa new-model",
                            "ip ssh version 2",
                            "line vty 0 4",
                            " transport input ssh",
                            "end"
                    )
            );

            configRepo.saveAll(List.of(c1, c2));
            log.info("Seeded 2 baseline configurations into database.");
        }
    }

    private void seedAudits() {
        if (auditRepo.count() == 0) {
            Audit a1 = new Audit();
            a1.setId("aud-001");
            a1.setAuditNumber("AUD-2026-001");
            a1.setName("Quarterly Perimeter Audit - edge-router-01");
            a1.setDeviceId("dev-001");
            a1.setConfigurationId("cfg-001");
            a1.setStatus("COMPLETED");
            a1.setDate("2026-03-28");
            a1.setDuration("3.4s");
            AuditSummary s1 = new AuditSummary();
            s1.setTotalFindings(3);
            s1.setCriticalFindings(0);
            s1.setHighFindings(2);
            s1.setMediumFindings(1);
            s1.setLowFindings(0);
            s1.setComplianceScore(82.0);
            s1.setTotalControls(4);
            s1.setPassedControls(1);
            s1.setFailedControls(3);
            a1.setSummary(s1);
            AuditScope sc1 = new AuditScope(List.of("dev-001"), List.of("cfg-001"), List.of("CIS Cisco IOS XE Benchmark", "NIST SP 800-53"));
            a1.setScope(sc1);
            a1.setFindingIds(List.of("find-001", "find-002", "find-003"));

            Audit a2 = new Audit();
            a2.setId("aud-002");
            a2.setAuditNumber("AUD-2026-002");
            a2.setName("Core Infrastructure Audit - core-switch-01");
            a2.setDeviceId("dev-002");
            a2.setConfigurationId("cfg-002");
            a2.setStatus("COMPLETED");
            a2.setDate("2026-03-25");
            a2.setDuration("2.1s");
            AuditSummary s2 = new AuditSummary();
            s2.setTotalFindings(0);
            s2.setComplianceScore(96.0);
            s2.setTotalControls(4);
            s2.setPassedControls(4);
            s2.setFailedControls(0);
            a2.setSummary(s2);
            AuditScope sc2 = new AuditScope(List.of("dev-002"), List.of("cfg-002"), List.of("CIS Cisco IOS XE Benchmark"));
            a2.setScope(sc2);

            auditRepo.saveAll(List.of(a1, a2));
            log.info("Seeded 2 baseline audits into database.");
        }
    }

    private void seedFindings() {
        if (findingRepo.count() == 0) {
            Finding f1 = new Finding();
            f1.setId("find-001");
            f1.setAuditId("aud-001");
            f1.setDeviceId("dev-001");
            f1.setConfigurationId("cfg-001");
            f1.setControl("cis-1-1-3");
            f1.setControlId("cis-1-1-3");
            f1.setRule("RULE-MGMT-TELNET-OFF");
            f1.setRuleId("RULE-MGMT-TELNET-OFF");
            f1.setTitle("Telnet service enabled on VTY lines");
            f1.setDescription("Cleartext Telnet protocol permitted on remote administration lines vty 0 4.");
            f1.setSeverity("High");
            f1.setStatus("OPEN");
            f1.setLine(21);
            f1.setFramework("CIS Cisco IOS XE Benchmark");
            f1.setExpected("transport input ssh");
            f1.setActual("transport input telnet ssh");
            f1.setImpact("Cleartext credentials and traffic subject to eavesdropping.");
            f1.setCanonicalField("management.remote_access.telnet_disabled");
            f1.setRemediation(List.of("configure terminal", "line vty 0 4", "transport input ssh", "end"));

            Finding f2 = new Finding();
            f2.setId("find-002");
            f2.setAuditId("aud-001");
            f2.setDeviceId("dev-001");
            f2.setConfigurationId("cfg-001");
            f2.setControl("cis-1-1-1");
            f2.setControlId("cis-1-1-1");
            f2.setRule("RULE-MGMT-SSH-V2");
            f2.setRuleId("RULE-MGMT-SSH-V2");
            f2.setTitle("Legacy SSH Version 1 permitted");
            f2.setDescription("SSH Version 1 is enabled which has known cryptanalytic vulnerabilities.");
            f2.setSeverity("High");
            f2.setStatus("OPEN");
            f2.setLine(12);
            f2.setFramework("CIS Cisco IOS XE Benchmark");
            f2.setExpected("ip ssh version 2");
            f2.setActual("ip ssh version 1");
            f2.setImpact("Cryptographic degradation allows man-in-the-middle session hijacking.");
            f2.setCanonicalField("management.remote_access.ssh_v2");
            f2.setRemediation(List.of("configure terminal", "ip ssh version 2", "end"));

            Finding f3 = new Finding();
            f3.setId("find-003");
            f3.setAuditId("aud-001");
            f3.setDeviceId("dev-001");
            f3.setConfigurationId("cfg-001");
            f3.setControl("cis-1-1-2");
            f3.setControlId("cis-1-1-2");
            f3.setRule("RULE-AUTH-AAA");
            f3.setRuleId("RULE-AUTH-AAA");
            f3.setTitle("AAA subsystem not initialized");
            f3.setDescription("Centralized AAA authorization and accounting is disabled ('no aaa new-model').");
            f3.setSeverity("Medium");
            f3.setStatus("ACKNOWLEDGED");
            f3.setLine(9);
            f3.setFramework("CIS Cisco IOS XE Benchmark");
            f3.setExpected("aaa new-model");
            f3.setActual("no aaa new-model");
            f3.setImpact("Lack of centralized authentication leads to unmonitored privilege escalation.");
            f3.setCanonicalField("authentication.aaa_enabled");
            f3.setRemediation(List.of("configure terminal", "aaa new-model", "end"));

            findingRepo.saveAll(List.of(f1, f2, f3));
            log.info("Seeded 3 baseline findings into database.");
        }
    }

    private void seedDriftEvents() {
        if (driftEventRepo.count() == 0) {
            DriftEvent d1 = new DriftEvent();
            d1.setId("drift-001");
            d1.setDeviceId("dev-001");
            d1.setVersion(2);
            d1.setDate("2026-03-27");
            d1.setChange("Transport protocol downgraded on line vty 0 4");
            d1.setDescription("Line vty transport changed from 'transport input ssh' to 'transport input telnet ssh'.");
            d1.setImpact("Increased");
            d1.setRiskBefore(35);
            d1.setRiskAfter(48);
            d1.setFinding("find-001");
            d1.setControls(List.of("cis-1-1-3"));

            driftEventRepo.saveAll(List.of(d1));
            log.info("Seeded 1 baseline drift event into database.");
        }
    }
}
