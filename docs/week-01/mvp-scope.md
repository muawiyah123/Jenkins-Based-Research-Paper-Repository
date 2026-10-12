# MVP Scope & Problem Definition
## Jenkins-Based Research Paper Repository

**Student Name:** Muawiyah Narvel  
**Roll Number:** 23108A0020  
**Project Title:** Jenkins-Based Research Paper Repository  
**Academic Term:** Week 1 Deliverable Milestone  
**Document Reference:** `docs/week-01/mvp-scope.md`  
**Status:** Approved 15-Task MVP Baseline  

---

## 1. Executive Summary & Real-Time Need
Academic institutions generate a substantial volume of technical papers, student dissertations, and conference preprints. Current departmental workflows rely on decentralized spreadsheets, local folders, or untracked email threads. This results in version confusion, lost abstracts, unindexed metadata, and opaque peer-review progression. 

The **Jenkins-Based Research Paper Repository** solves this real-time need by providing a centralized web repository for recording paper metadata, managing peer review status transitions, and providing public catalog discoverability. In addition, it establishes a reliable engineering pipeline using Jenkins CI, Selenium testing, Docker packaging, and Ansible configuration management.

---

## 2. Stakeholder List & Target Users

| Stakeholder Group | Primary Interests & System Role |
| :--- | :--- |
| **Student Researchers & Authors** | Submit research paper metadata; view public repository; edit submissions during draft/revision stages; track peer-review status. |
| **Faculty & Peer Reviewers** | Inspect paper abstracts and metadata; advance papers through formal review stages (`UNDER_REVIEW`, `ACCEPTED`, `REJECTED`, `REVISION_REQUESTED`). |
| **Academic Administrators** | Oversee repository throughput via dashboard metrics; audit institutional research output. |
| **College Faculty & Evaluators** | Assess software architecture, Java 21 compliance, build reproducibility, and DevOps delivery pipeline repeatability. |
| **Public / Guest Readers** | Browse the catalog, search publications by title/author/keyword, and read abstracts without authentication. |

---

## 3. Project Objectives & Measurable Success Criteria

### 3.1 Objectives
1. Build a functional, server-rendered web application using Java 21, Spring Boot 3, Thymeleaf, and Spring Data JPA.
2. Provide public search and browsing across research metadata while safeguarding author privacy (`submitterEmail` is never exposed publicly).
3. Enforce a strict review state machine where status transitions are restricted to Reviewers, and authors can resubmit revisions.
4. Establish an automated DevOps delivery pipeline across Weeks 4–14 (Git branching, Jenkins CI, Selenium tests, Docker, Ansible).

### 3.2 Measurable Success Criteria
- **Build Reproducibility:** Single-command build via `./mvnw clean package` passing with 0 compilation errors and 0 test failures.
- **Latency Target (Provisional):** Localhost page render latency typically under 500 ms for Home (`GET /`) and Catalog (`GET /papers`).
- **Validation Fidelity:** 100% of invalid form inputs (short titles, invalid years, malformed emails) trigger inline user-friendly errors without server crashes.
- **State Integrity:** Zero unauthorized or illegal review status transitions permitted by the server.
- **Delivery Automation:** Verified progression through all 14 weekly college deliverables without unverified claims.

---

## 4. Project Constraints
1. **Runtime & Language Target:** Java 21 LTS with Spring Boot 3.x.
2. **Build Tool:** Apache Maven with Maven Wrapper (`mvnw.cmd` on Windows).
3. **Database Architecture:** Embedded H2 in-memory relational database for initial development; data resets upon restart.
4. **Scope Boundaries:** Record deletion, administrator overrides, audit logs, file uploads, AI summarization, and plagiarism detection are strictly excluded.
5. **Infrastructure Dependencies:** Target host environment for Ansible execution and Tomcat/Nginx deployment model are flagged for faculty confirmation.

---

## 5. Frozen 15-Task MVP Scope

To satisfy the official Week 1 curriculum requirements, the core application MVP is frozen into exactly 15 foundational tasks:

| Task ID | Task Title | Description & Linked Requirement | Target Phase |
| :---: | :--- | :--- | :---: |
| **TASK-01** | Project Scaffolding & Setup | Initialize Spring Boot 3 + Java 21 structure with Maven Wrapper and dependencies (Web, Thymeleaf, JPA, H2, Validation). | Week 3 |
| **TASK-02** | Responsive Home Page | Implement home page controller, Thymeleaf template, Bootstrap styling, and safe placeholder navigation (`US-001`). | Week 3 |
| **TASK-03** | Paper Entity & Schema Mapping | Define `Paper` JPA entity with all 12 fields (including `submitterUsername`) and H2 database table mapping (`FR-08`, `US-002`). | Week 4 |
| **TASK-04** | JPA Repository & Data Access | Implement `PaperRepository` with Spring Data JPA for CRUD persistence and custom query execution (`FR-08`, `US-002`). | Week 4 |
| **TASK-05** | Security Baseline & In-Memory Auth | Configure Spring Security with mock accounts (`researcher`, `reviewer`, `admin`) and public route matching (`FR-07`, `US-009`). | Week 4 |
| **TASK-06** | Public Catalog Listing | Build `GET /papers` displaying registered papers in a responsive table without exposing submitter contact details (`FR-02`, `US-003`). | Week 5 |
| **TASK-07** | Public Paper Detail View | Build `GET /papers/{id}` rendering full abstract, keywords, and metadata while protecting author privacy (`FR-02`, `US-003`). | Week 5 |
| **TASK-08** | Paper Submission Form | Create validated `GET /papers/new` and `POST /papers` for authenticated Researchers; derive `submitterUsername` from principal (`FR-01`, `US-004`). | Week 5 |
| **TASK-09** | Author Metadata Editing | Implement `GET/POST /papers/{id}/edit` restricted to original author while in `SUBMITTED` or `REVISION_REQUESTED` (`FR-03`, `US-005`). | Week 6 |
| **TASK-10** | Public Multi-Attribute Search | Implement case-insensitive search by title, author, and keyword with empty/no-results handling (`FR-04`, `US-006`). | Week 6 |
| **TASK-11** | Review Status State Machine | Implement service-layer review workflow transitions and server-side validation against illegal jumps (`FR-05`, `US-007`). | Week 7 |
| **TASK-12** | Reviewer Status Operations | Create reviewer UI and endpoint (`POST /papers/{id}/status`) restricted to `ROLE_REVIEWER` (`FR-05`, `US-007`). | Week 7 |
| **TASK-13** | Author Revision Resubmission | Enable author resubmission from `REVISION_REQUESTED` back to `UNDER_REVIEW` upon metadata edit (`FR-03`, `FR-05`, `US-005`, `US-007`). | Week 7 |
| **TASK-14** | Metrics Dashboard | Implement `GET /dashboard` displaying metric count cards for all 5 review statuses plus Total Papers (`FR-06`, `US-008`). | Week 7 |
| **TASK-15** | Input Validation & Error Handling | Enforce Bean Validation constraints and configure custom `404.html` and global exception handling (`US-010`). | Week 7 |

---

## 6. Excluded Features (Out-of-Scope)
- Paper deletion or soft-delete UI (explicitly excluded).
- Administrator status overrides, user-role management consoles, or historical audit log tables.
- Plagiarism detection algorithms or integration with external checking services.
- AI paper summarization, text extraction, or generative tools.
- Direct binary file upload/streaming (PDF, Word, OCR).
- Multi-node clustering or distributed cloud databases.

---

## 7. Conditions Required Before Week 2
Before commencing Week 2 (Agile Planning & DevOps Workflow), verify:
1. `problem-statement.md` is approved and verified.
2. `software-requirements-specification.md` is approved and verified.
3. `user-stories.md`, `requirements-traceability.md`, and `backlog-summary.md` are aligned with the 15-task scope.
4. `mvp-scope.md` is reviewed and approved.
5. All open faculty clarification items are formally tracked.
