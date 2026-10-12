# Requirements Traceability Matrix
## Jenkins-Based Research Paper Repository

**Student Name:** Muawiyah Narvel  
**Roll Number:** 23108A0020  
**Project Title:** Jenkins-Based Research Paper Repository  
**Document Reference:** `docs/week-01/requirements-traceability.md`  
**Status:** Approved Baseline  

---

## 1. Traceability Purpose
The Requirements Traceability Matrix (RTM) establishes bidirectional traceability between the approved Software Requirements Specification (SRS), the backlog User Stories, testable Acceptance Criteria, proposed verification methods, and planned target evidence artifacts.

---

## 2. Requirements Traceability Matrix

| SRS Req ID | SRS Requirement Name | User Story ID | User Story Title | Acceptance Criteria ID | Verification Method | Planned Target Evidence |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **FR-01** | Create Paper Record | **US-004** | Create Research Paper Record | `AC-004-1`, `AC-004-2` | Spring Boot MockMvc form test with `@WithMockUser(username="researcher", roles="RESEARCHER")` verifying server-derived `submitterUsername` | Terminal form submission test log & catalog listing view |
| **FR-02** | Catalog Listing & Detail View | **US-003** | Public Catalog Listing and Paper Details | `AC-003-1`, `AC-003-2`, `AC-003-3`, `AC-003-4`, `AC-003-5` | Spring MVC unauthenticated `MockMvc` GET test asserting HTTP 200 and absence of submitter contact email | Browser screenshot of catalog table and public detail view (no email exposed) |
| **FR-03** | Edit Paper Metadata | **US-005** | Edit Paper Metadata (Author Only & Resubmission) | `AC-005-1`, `AC-005-2`, `AC-005-3`, `AC-005-4`, `AC-005-5` | Service unit test & Security MockMvc testing author edits, resubmission to UNDER_REVIEW, and 403 on unauthorized attempts | Test execution log verifying author edits, resubmission transition, and HTTP 403 on unauthorized attempts |
| **FR-04** | Search & Filtering | **US-006** | Public Multi-Attribute Search | `AC-006-1`, `AC-006-2`, `AC-006-3`, `AC-006-4`, `AC-006-5`, `AC-006-6` | Spring Data JPA query integration test & MVC search controller test | Surefire search test report & browser search result screenshots |
| **FR-05** | Manage Review Status | **US-007** | Review Status State Machine & Management | `AC-007-1`, `AC-007-2`, `AC-007-3`, `AC-007-4`, `AC-007-5` | Isolated JUnit 5 domain service unit tests & MockMvc security test | Surefire test execution report validating all state transition rules |
| **FR-06** | Dashboard Summary | **US-008** | Repository Metrics Dashboard | `AC-008-1`, `AC-008-2`, `AC-008-3` | Spring MVC MockMvc test & JPA count aggregation query test | Test execution log & browser screenshot of dashboard with all 6 metric cards |
| **FR-07** | Backend Authorization for Protected Operations | **US-009** | In-Memory Development Authentication & Role Guards | `AC-009-1`, `AC-009-2`, `AC-009-3`, `AC-009-4` | Spring Security MockMvc integration tests using mock user principals & principal ownership check (`submitterUsername`) | Security test logs verifying public endpoints and HTTP 403 on unauthorized access |
| **FR-08** | Database Persistence | **US-002** | Paper Entity Relational Persistence | `AC-002-1`, `AC-002-2` | Spring Boot `@DataJpaTest` with embedded H2 database | Surefire test report confirming H2 schema generation and CRUD persistence |
| **NFR-01** | Build Automation | **US-012** | Maven Build Automation & Test Pipeline | `AC-012-1`, `AC-012-2` | Command line `./mvnw clean package` execution | Console output showing `BUILD SUCCESS` and generated JAR artifact |
| **NFR-02** | Runtime Target (Java 21 LTS) | **US-001**, **US-012** | Home Page Display & Maven Build Automation | `AC-001-1`, `AC-012-1` | Command line `java --version` and compiler execution | Terminal output verifying Java 21 LTS runtime |
| **NFR-03** | Continuous Integration | **US-013** | Jenkins Declarative Pipeline Automation | `AC-013-1`, `AC-013-2` | Jenkins declarative pipeline execution on commit push | Jenkins console build log and Stage View screenshot |
| **NFR-04** | Automated Test Coverage (Planned Target) | **US-002**, **US-007**, **US-012** | JPA, Workflow, and Maven Testing | `AC-002-1`, `AC-007-1`, `AC-012-1` | Maven Surefire test execution (Week 3: context-load only; Weeks 4–7: domain coverage) | Surefire HTML / XML test reports upon actual test execution |
| **NFR-05** | Response Latency (Provisional Target) | **US-001**, **US-003** | Initial Home Page & Catalog Listing | `AC-001-3`, `AC-003-5` | Local browser DevTools network tab / curl latency benchmark on localhost (provisional design target; unverified until measured) | DevTools network waterfall screenshot / curl latency log upon execution |
| **NFR-06** | UI Responsiveness & Usability | **US-001**, **US-010** | Home Page & Input Validation | `AC-001-2`, `AC-010-1` | Viewport rendering across desktop (1080p) and mobile | Responsive browser screenshots |
| **NFR-07** | Security & Credential Hygiene | **US-011** | Git Version Control Initialization | `AC-011-3` | Inspection of tracked files (`git ls-files`), `.gitignore` verification, and Git history audit (`git log`) without exposing secret values | Clean Git status and history verification log without leaking sensitive tokens |

---

## 3. Completeness & Gap Analysis

### 3.1 SRS Coverage Verification
- **Total Functional Requirements (SRS):** 8 (FR-01 to FR-08)
- **Functional Requirements Mapped:** 8 (100% coverage)
- **Total Non-Functional Requirements (SRS):** 7 (NFR-01 to NFR-07)
- **Non-Functional Requirements Mapped:** 7 (100% coverage)
- **Unmapped SRS Requirements:** **0**

### 3.2 Backlog Story Justification
- **Stories supporting MVP Functional Requirements:** US-002, US-003, US-004, US-005, US-006, US-007, US-008, US-009, US-010.
- **Stories supporting Week 1–3 Foundation & Build:** US-001, US-011, US-012.
- **Stories supporting Planned DevOps Pipeline (Weeks 8–15):** US-013, US-014, US-015, US-016, US-017.
- **Unjustified or Orphan Stories:** **0** (Every story directly implements an approved SRS requirement or is an essential infrastructure task supporting delivery).
