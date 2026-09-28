package com.nexuscomply.framework.service;

import com.nexuscomply.common.exception.ResourceNotFoundException;
import com.nexuscomply.framework.model.ComplianceRule;
import com.nexuscomply.framework.model.Control;
import com.nexuscomply.framework.model.Framework;
import com.nexuscomply.framework.model.FrameworkMapping;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FrameworkServiceImpl implements FrameworkService {

    private final Map<String, Framework> frameworkStore = new ConcurrentHashMap<>();
    private final Map<String, Control> controlStore = new ConcurrentHashMap<>();
    private final Map<String, ComplianceRule> ruleStore = new ConcurrentHashMap<>();
    private final Map<String, FrameworkMapping> mappingStore = new ConcurrentHashMap<>();

    @PostConstruct
    public void initDefaultFrameworks() {
        // Framework 1: CIS Cisco IOS XE
        Framework cis = new Framework(
                "cis-cisco-ios-xe",
                "CIS_CISCO_IOS_XE",
                "CIS Cisco IOS XE Benchmark",
                "2.0.0",
                "BENCHMARK",
                "Security baseline recommendations for Cisco IOS XE network equipment.",
                2,
                List.of("Cisco")
        );
        frameworkStore.put(cis.getId(), cis);

        // Framework 2: NIST SP 800-53 R5
        Framework nist = new Framework(
                "nist-sp-800-53-r5",
                "NIST_800_53_R5",
                "NIST SP 800-53 Rev 5",
                "Rev 5",
                "REGULATORY",
                "Security and Privacy Controls for Information Systems and Organizations.",
                1,
                List.of("Cisco", "Juniper", "Fortinet")
        );
        frameworkStore.put(nist.getId(), nist);

        // Control 1: CIS 1.1.1 SSH
        Control ctrlSsh = new Control(
                "cis-1-1-1",
                cis.getId(),
                "1.1.1",
                "Ensure 'transport input ssh' is configured",
                "Disables unencrypted remote administration protocols like Telnet.",
                "HIGH",
                "ACCESS_CONTROL",
                "Configure 'line vty 0 4' with 'transport input ssh'.",
                List.of("rule-ssh-01")
        );
        controlStore.put(ctrlSsh.getId(), ctrlSsh);

        // Control 2: CIS 1.1.2 AAA
        Control ctrlAaa = new Control(
                "cis-1-1-2",
                cis.getId(),
                "1.1.2",
                "Ensure 'aaa new-model' is enabled",
                "Enables AAA access control subsystem for authentication.",
                "CRITICAL",
                "AUTHENTICATION",
                "Configure 'aaa new-model' in global configuration.",
                List.of("rule-aaa-01")
        );
        controlStore.put(ctrlAaa.getId(), ctrlAaa);

        // Control 3: NIST AC-17 Remote Access
        Control ctrlNistAc17 = new Control(
                "nist-ac-17",
                nist.getId(),
                "AC-17",
                "Remote Access Management",
                "Authorize and monitor remote access connections using cryptographic mechanisms.",
                "HIGH",
                "ACCESS_CONTROL",
                "Use SSH v2 and enforce cryptographic network management.",
                List.of("rule-ssh-01")
        );
        controlStore.put(ctrlNistAc17.getId(), ctrlNistAc17);

        // Rule 1: SSH Rule
        ComplianceRule ruleSsh = new ComplianceRule(
                "rule-ssh-01",
                ctrlSsh.getId(),
                "SSH Version 2 Enforcement",
                "Verifies that SSH is enabled and Telnet is disabled",
                "security.ssh.enabled",
                "EQUALS",
                true,
                "HIGH"
        );
        ruleStore.put(ruleSsh.getId(), ruleSsh);

        // Rule 2: AAA Rule
        ComplianceRule ruleAaa = new ComplianceRule(
                "rule-aaa-01",
                ctrlAaa.getId(),
                "AAA Subsystem Verification",
                "Verifies that AAA new-model is enabled",
                "security.aaa.enabled",
                "EQUALS",
                true,
                "CRITICAL"
        );
        ruleStore.put(ruleAaa.getId(), ruleAaa);

        // Mapping 1: CIS 1.1.1 -> NIST AC-17
        FrameworkMapping map1 = new FrameworkMapping(
                "map-cis-nist-01",
                cis.getId(),
                ctrlSsh.getId(),
                nist.getId(),
                ctrlNistAc17.getId(),
                "EQUIVALENT",
                0.95
        );
        mappingStore.put(map1.getId(), map1);
    }

    @Override
    public List<Framework> getAllFrameworks() {
        return new ArrayList<>(frameworkStore.values());
    }

    @Override
    public Framework getFrameworkById(String id) {
        Framework f = frameworkStore.get(id);
        if (f == null) {
            throw new ResourceNotFoundException("Framework", id);
        }
        return f;
    }

    @Override
    public List<Control> getControlsByFramework(String frameworkId) {
        getFrameworkById(frameworkId); // validate framework exists
        return controlStore.values().stream()
                .filter(c -> frameworkId.equals(c.getFrameworkId()))
                .toList();
    }

    @Override
    public Control getControlById(String id) {
        Control c = controlStore.get(id);
        if (c == null) {
            throw new ResourceNotFoundException("Control", id);
        }
        return c;
    }

    @Override
    public List<ComplianceRule> getRulesByControl(String controlId) {
        getControlById(controlId); // validate control exists
        return ruleStore.values().stream()
                .filter(r -> controlId.equals(r.getControlId()))
                .toList();
    }

    @Override
    public ComplianceRule getRuleById(String id) {
        ComplianceRule r = ruleStore.get(id);
        if (r == null) {
            throw new ResourceNotFoundException("Rule", id);
        }
        return r;
    }

    @Override
    public List<FrameworkMapping> getAllMappings() {
        return new ArrayList<>(mappingStore.values());
    }

    @Override
    public FrameworkMapping getMappingById(String id) {
        FrameworkMapping m = mappingStore.get(id);
        if (m == null) {
            throw new ResourceNotFoundException("Framework mapping", id);
        }
        return m;
    }
}
