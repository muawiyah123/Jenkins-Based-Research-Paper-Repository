# Backlog Summary & Phased Implementation Plan
## Jenkins-Based Research Paper Repository

**Student Name:** Muawiyah Narvel  
**Roll Number:** 23108A0020  
**Project Title:** Jenkins-Based Research Paper Repository  
**Document Reference:** `docs/week-01/backlog-summary.md`  
**Status:** Approved Baseline  

---

## 1. Executive Summary
This document summarizes the product backlog for the **Jenkins-Based Research Paper Repository**. It organizes 17 user stories into clearly demarcated delivery phases, enforces strict architectural dependencies, records risk mitigations, and tracks open faculty clarification items.

---

## 2. Categorization: MVP vs. Planned vs. Out-of-Scope

### 2.1 Weeks 1–3 Foundation Milestone (Initial Working Scaffold)
*Goal: Minimal running application with home page, Maven build verification, and clean version control.*
- **US-001:** Initial Home Page Display (Spring Boot, Thymeleaf, Bootstrap; placeholder navigation links to unbuilt features must remain inert and not expose unbuilt protected routes; provisional latency target < 500 ms)
- **US-011:** Git Version Control Initialization, `.gitignore`, Clean Commit (Verification audits tracked files and history without exposing secrets)
- **US-012:** Maven Build Automation & Wrapper (`mvnw.cmd clean package`; Week 3 acceptance gate verifies context-load test only, domain coverage planned for Weeks 4–7)

### 2.2 Weeks 4–7 Application MVP Implementation
*Goal: Full paper metadata management, review workflows, search, and security.*
- **US-009:** In-Memory Development Authentication & Role Guards (Security baseline; defines accounts `researcher`, `reviewer`, `admin`)
- **US-002:** Paper Entity Relational Persistence (Spring Data JPA, H2; schema includes `submitter_username`)
- **US-003:** Public Catalog Listing and Paper Details (Privacy-protected; never exposes `submitterEmail` or private contact info; provisional latency target < 500 ms)
- **US-004:** Create Research Paper Record (`ROLE_RESEARCHER`; `submitterUsername` derived server-side from `Authentication.getName()` as sole source of ownership; `submitterEmail` is contact metadata only)
- **US-005:** Edit Paper Metadata & Resubmission (Author-only verified via `submitterUsername`, status-locked; resubmission transitions from `REVISION_REQUESTED` to `UNDER_REVIEW`)
- **US-006:** Public Multi-Attribute Search (Case-insensitive, Title/Author/Keyword, empty & no-results handling)
- **US-007:** Review Status State Machine & Management (Reviewer & author resubmission, immutable terminal states)
- **US-008:** Repository Metrics Dashboard (All 5 statuses + Total)
- **US-010:** Input Validation & User-Friendly Error Handling (`404.html`)

### 2.3 Weeks 8–15 Planned DevOps Pipeline & Final Milestones
*Goal: Automated delivery, testing, containerization, and configuration management.*
- **US-013:** Jenkins Declarative Pipeline Automation (`Jenkinsfile`)
- **US-014:** Automated End-to-End UI Smoke Tests (Selenium WebDriver)
- **US-015:** Multi-Stage Container Packaging & Health Verification (Docker)
- **US-016:** Automated Host Provisioning & Deployment Playbook (Ansible; target host environment subject to faculty clarification)
- **US-017:** Comprehensive Evidence Portfolio & Viva Demonstration (100% test pass is a final target, not a current result)

### 2.4 Strictly Out-of-Scope (All Weeks)
- Paper deletion or soft-delete UI.
- Administrator status overrides and historical audit log tables.
- Plagiarism detection algorithms or third-party turnitin integrations.
- AI-based summarization or LLM capabilities.
- Direct PDF/DOCX binary parsing or OCR indexing.
- Microservices, Kubernetes clusters, or multi-cloud orchestrations.

---

## 3. Dependency-Enforced Implementation Order

To ensure architectural integrity, implementation must follow this strict sequence:

```text
Step 1 (Week 3): Foundation Scaffold
  ├── US-001 (Spring Boot + Home Page)
  ├── US-012 (Maven Wrapper Build Verification)
  └── US-011 (Git Init & Clean Commit)

Step 2 (Week 4): Security & Persistence Foundation
  ├── US-009 (Spring Security with Development Accounts)
  └── US-002 (Paper Entity, JPA Repository, H2 Schema)

Step 3 (Week 5): Public Browsing & Paper Creation
  ├── US-003 (Public Catalog & Detail View, no email displayed)
  └── US-004 (Paper Submission Form & Researcher Protection)

Step 4 (Week 6): Editing, Search & Validation
  ├── US-005 (Author Metadata Editing, Resubmission & State Guards)
  ├── US-006 (Public Multi-Attribute Case-Insensitive Search)
  └── US-010 (Form Validation Annotations & Error Pages)

Step 5 (Week 7): Workflow State Machine & Dashboard
  ├── US-007 (Review Status Transitions & Resubmission)
  └── US-008 (Dashboard Aggregation Metrics)

Step 6 (Weeks 8–15): DevOps Pipeline Automation
  ├── US-013 (Jenkins CI)
  ├── US-014 (Selenium UI Tests)
  ├── US-015 (Docker Image)
  ├── US-016 (Ansible Playbooks)
  └── US-017 (Final Viva Rehearsal & Evidence Compilation)
```

---

## 4. Major Risks & Mitigation Strategies

| Risk ID | Risk Description | Severity | Mitigation Strategy |
| :--- | :--- | :--- | :--- |
| **RSK-01** | Scope Creep / Over-Engineering | High | Strictly enforce MVP boundary (US-001 to US-012); defer file uploads, AI, and microservices. |
| **RSK-02** | Ephemeral In-Memory Data Reset | Medium | Clearly document that in-memory H2 resets on restart; use `@Sql` test fixtures or seeds for repeatable test runs. |
| **RSK-03** | Local vs. CI Environment Drift | Medium | Use Maven Wrapper (`mvnw`) to eliminate global Maven discrepancies; enforce Java 21 across developer workstation and Jenkins agent. |
| **RSK-04** | Selenium Browser Driver Mismatches | Medium | Use headless browser mode and WebDriverManager/Selenium 4 automated driver resolution during Week 10. |
| **RSK-05** | Premature Evidence Reporting | Low | Mark all evidence as planned until real execution outputs, test logs, or screenshots are verified. US-017 test pass is a final target, not a current result. |

---

## 5. Items Flagged for Faculty Clarification

1. **Deployment Architecture:** Confirm whether Spring Boot's embedded Tomcat containerized inside Docker behind an Nginx reverse proxy satisfies the college syllabus requirement for "Tomcat/Nginx deployment".
2. **Database Engine Longevity:** Confirm whether in-memory / file-backed H2 satisfies evaluation criteria for the final viva, or if a dedicated PostgreSQL database container must be demonstrated.
3. **Security Model:** Confirm whether in-memory development user accounts (`researcher`, `reviewer`, `admin`) are acceptable for the initial security evaluation.
4. **Ansible Target-Host Environment:** Confirm the intended target host environment (local VM, secondary physical machine, or container host) for executing Ansible configuration management playbooks.
