# Academic Project Report: Jenkins-Based Research Paper Repository

**Candidate Details:**
- **Student Name:** Muawiyah Sahil Narvel
- **Roll Number:** 23108A0020
- **Course / Lab:** DevOps & Cloud Automation / Software Engineering Laboratory
- **GitHub Repository:** [muawiyah123/Jenkins-Based-Research-Paper-Repository](https://github.com/muawiyah123/Jenkins-Based-Research-Paper-Repository.git)
- **Active Branches:** `main` (Production/Release Baseline), `development` (Active Development)
- **Application URL:** `http://localhost:8081` | **Jenkins CI URL:** `http://localhost:8080`

---

## Executive Summary

The **Jenkins-Based Research Paper Repository** is an enterprise-grade academic publishing system engineered with **Spring Boot 3.4.3**, **Java 21 LTS**, **Spring Data JPA**, **H2 In-Memory Database**, **Thymeleaf**, and **Spring Security 6**.

The system replaces manual, error-prone email submissions with a structured, role-governed peer-review workflow backed by an automated **DevOps Continuous Integration and Deployment (CI/CD)** pipeline. The project encompasses 15 structured implementation tasks across 4 milestone phases, including headless end-to-end (E2E) browser testing with Selenium WebDriver, containerization with Docker, and provisioning automation via Ansible.

All **79 automated tests** (76 unit/integration/security slice tests and 3 Selenium E2E smoke tests) execute with a **100% pass rate**, verified both locally and within a multi-stage declarative Jenkins pipeline.

---

## 1. System Architecture & Technical Specifications

```mermaid
graph TD
    User([End User / Browser]) -->|HTTP :8081| App[Spring Boot 3.4.3 Web Layer]
    App --> Sec[Spring Security 6 RBAC Filter Chain]
    Sec -->|researcher / reviewer / admin| Ctrl[MVC Controllers]
    Ctrl --> Svc[PaperService State Machine & Business Logic]
    Svc --> Repo[PaperRepository JPA Layer]
    Repo --> DB[(H2 In-Memory Database :paperdb)]
    
    Dev([Developer / Git Push]) -->|git push origin main| GitHub[GitHub Remote Repository]
    Jenkins[Jenkins CI Server :8080] -->|SCM Polling / Webhook| GitHub
    Jenkins -->|Checkout -> Verify -> Compile -> Test -> Package| BuildArtifacts[JAR: repository-0.0.1-SNAPSHOT.jar]
    
    Docker[Docker Container Engine] -.->|Container Image| App
    Ansible[Ansible Automation] -.->|Playbook Deployment| Docker
```

### Technology Stack Matrix
| Component | Technology | Version / Specification | Rationale & Purpose |
|---|---|---|---|
| **Programming Language** | Java | 21 LTS (Eclipse Temurin) | Modern pattern matching, records, long-term support |
| **Framework** | Spring Boot | 3.4.3 | Standalone microservice framework with auto-configuration |
| **Data Persistence** | Spring Data JPA / Hibernate | 6.x | Object-Relational Mapping (ORM) and declarative queries |
| **Database** | H2 Database | 2.3.232 (In-Memory) | Zero-footprint, ephemeral persistence for rapid CI execution |
| **Security Layer** | Spring Security | 6.x | RBAC (`researcher`, `reviewer`, `admin`), CSRF protection |
| **Template Engine** | Thymeleaf + Bootstrap 5 | 5.3.3 | Server-side rendering, responsive glassmorphism UI |
| **CI Automation** | Jenkins CI | 2.580.1 | Declarative pipeline for build, test, and artifact archiving |
| **E2E Testing** | Selenium WebDriver | 4.28.1 | Headless Chrome automated user-journey validation |
| **Containerization** | Docker & Docker Compose | Multi-stage build | Portable deployment with non-root security execution |
| **Configuration Mgmt** | Ansible | Playbook (`deploy.yml`) | Declarative container and host provisioning |

---

## 2. Comprehensive 15-Task Implementation Log

### Phase 1: Foundation & Planning (Weeks 1–3)

#### Task 1: Environment & Toolchain Verification
- **User Story:** US-001 (Developer Toolchain Initialization)
- **Objective:** Establish the foundational Java 21 LTS runtime, Apache Maven wrapper (`mvnw.cmd`), and Git version control environment.
- **Implementation:** Verified JDK 21.0.12.1 Temurin, validated Maven wrapper dependencies, and established local port allocations (`8081` for the Spring Boot application to eliminate conflict with Jenkins on port `8080`).
- **Deliverables:** Working Maven build wrapper, system variable paths, and baseline compiler compatibility flags (`--release 21`).
- **Status:** **COMPLETED** (Verified via `mvnw -v`)

#### Task 2: Spring Boot Scaffolding & Build Verification
- **User Story:** US-001
- **Objective:** Initialize the project structure using Spring Boot Starter web, thymeleaf, data-jpa, validation, and h2.
- **Implementation:** Generated `pom.xml`, `RepositoryApplication.java`, and configured `application.properties` with database connection string `jdbc:h2:mem:paperdb;DB_CLOSE_DELAY=-1`, enabling the H2 Web Console at `/h2-console`.
- **Deliverables:** Maven POM, Spring Boot main entry point, and green context-load test (`RepositoryApplicationTests.java`).
- **Status:** **COMPLETED** (1/1 test passed)

#### Task 3: Version Control Baseline & Repo Init
- **User Story:** US-001
- **Objective:** Configure repository structure, `.gitignore` rules, dual-branch Git model (`main` and `development`), and link to GitHub remote.
- **Implementation:** Initialized Git repository, configured exclusion patterns for target artifacts, IDE configurations, and temporary test directories. Pushed baseline to GitHub remote.
- **Deliverables:** `.gitignore`, initial Git commits, synchronized `main` and `development` branches.
- **Status:** **COMPLETED**

#### Task 4: Public Home & Navigation Scaffold
- **User Story:** US-001 (Public Portal Landing)
- **Objective:** Create the public landing page with accessible navigation, platform statistics overview, and dynamic role-based headers.
- **Implementation:** Implemented `HomeController.java` handling `GET /`, mapping to `index.html` styled with custom modern UI, responsive hero layout, platform metrics preview cards, and authentication state toggles.
- **Deliverables:** `HomeController.java`, `templates/index.html`, `static/css/style.css`, slice tests in `HomeControllerTest.java`.
- **Status:** **COMPLETED** (3/3 MockMvc tests passing)

---

### Phase 2: Core Domain Model & Application MVP (Weeks 4–7)

#### Task 5: Domain Model & In-Memory Persistence
- **User Story:** US-002 (Domain Architecture)
- **Objective:** Construct the `Paper` entity with 12 distinct attributes, audit timestamps, and the `ReviewStatus` enumeration.
- **Implementation:**
  - `Paper.java`: ID, `title`, `authors`, `abstractText`, `keywords`, `publicationYear`, `doiUrl`, `submitterEmail`, `submitterUsername`, `reviewStatus`, `createdAt`, `updatedAt`.
  - `ReviewStatus.java`: `SUBMITTED`, `UNDER_REVIEW`, `ACCEPTED`, `REJECTED`, `REVISION_REQUESTED`.
  - `PaperRepository.java`: Custom JPA queries including case-insensitive keyword search, status filtering, and count aggregations.
- **Deliverables:** `Paper.java`, `ReviewStatus.java`, `PaperRepository.java`, and 5 JPA slice tests in `PaperRepositoryTest.java`.
- **Status:** **COMPLETED** (5/5 repository tests passed)

#### Task 6: Research Paper Submission Workflow
- **User Story:** US-002 (Author Paper Submission)
- **Objective:** Provide a secure submission form with robust server-side validation and automatic association of submitter credentials.
- **Implementation:**
  - Implemented `PaperController.java` handling `GET /papers/submit` and `POST /papers/submit`.
  - Created `PaperSubmissionDto.java` with `@NotBlank`, `@Size`, `@Min`, `@Max`, and regex DOI URL validators.
  - Submitter username automatically injected from `Authentication.getName()` to guarantee attribution integrity; initial status defaulted to `SUBMITTED`.
- **Deliverables:** `PaperController.java`, `submit.html`, validation unit tests in `PaperServiceTest.java`.
- **Status:** **COMPLETED** (15/15 MockMvc tests passed)

#### Task 7: Public Catalog & Real-Time Keyword Search
- **User Story:** US-003 (Public Catalog), US-004 (Keyword Discovery)
- **Objective:** Allow anonymous and authenticated users to search and browse papers with strict author email privacy protection.
- **Implementation:**
  - Built `GET /papers` and `GET /papers/{id}` endpoints.
  - Implemented multi-field SQL search spanning `title`, `authors`, and `keywords`.
  - **Privacy Rule:** The submitter's email address is strictly omitted from public catalog tables and public detail cards, rendering only public academic metadata.
- **Deliverables:** `catalog.html`, `detail.html`, search filter logic, privacy assertion tests.
- **Status:** **COMPLETED** (Search & privacy slice tests passing)

#### Task 8: Paper Metadata Edit & Author Resubmission
- **User Story:** US-005 (Paper Modification & Resubmission)
- **Objective:** Enable authors to modify papers that are in `SUBMITTED` or `REVISION_REQUESTED` status while restricting unauthorized tampering.
- **Implementation:**
  - `GET /papers/{id}/edit` and `POST /papers/{id}/edit`.
  - Enforced ownership verification (`submitterUsername == authentication.name`) and state transition rules (papers in `ACCEPTED` or `REJECTED` state cannot be edited).
  - Editing a paper in `REVISION_REQUESTED` state automatically resets status to `SUBMITTED` for re-evaluation.
- **Deliverables:** `edit.html`, ownership guard logic in `PaperService.java`, security exception handling.
- **Status:** **COMPLETED**

#### Task 9: In-Memory Spring Security 6 Role-Based Access Control (RBAC)
- **User Story:** US-009 (Authentication & Authorization)
- **Objective:** Secure application routes using Spring Security 6 with 3 in-memory development roles.
- **Implementation:**
  - `SecurityConfig.java` configured with `BCryptPasswordEncoder`.
  - Three pre-configured accounts:
    - `researcher` (Role: `RESEARCHER`) — Can submit, view catalog, and edit own submissions.
    - `reviewer` (Role: `REVIEWER`) — Can access review queue and transition paper statuses.
    - `admin` (Role: `ADMIN`, `REVIEWER`, `RESEARCHER`) — Can access all platform portals and metrics dashboard.
  - Configured custom login page at `/login` and logout handling with CSRF enabled on all mutating POST requests.
- **Deliverables:** `SecurityConfig.java`, `login.html`, 12 security slice tests in `SecurityConfigTest.java`.
- **Status:** **COMPLETED** (12/12 security tests passed)

#### Task 10: Peer-Review Workflow & State Machine
- **User Story:** US-006 (Reviewer Queue), US-007 (Status Decision Execution)
- **Objective:** Enforce an immutable, validated 5-state lifecycle state machine for academic paper reviews.
- **Implementation:**
  - `PaperController.java` with routes `/papers/review`, `/papers/{id}/review`, `/papers/{id}/status`.
  - State machine transitions:
    - `SUBMITTED` -> `UNDER_REVIEW`
    - `UNDER_REVIEW` -> `ACCEPTED` (Terminal)
    - `UNDER_REVIEW` -> `REJECTED` (Terminal)
    - `UNDER_REVIEW` -> `REVISION_REQUESTED`
    - `REVISION_REQUESTED` -> `SUBMITTED` (Triggered upon author resubmission)
  - Prevented illegal transitions (e.g. `ACCEPTED` -> `SUBMITTED`) by throwing `IllegalStateException`.
- **Deliverables:** `review-portal.html`, `review.html`, 10 state machine tests in `PaperServiceTest.java`.
- **Status:** **COMPLETED** (10/10 review tests passed)

#### Task 11: Administrative Metrics Dashboard
- **User Story:** US-008 (Admin Analytics)
- **Objective:** Provide repository administrators with real-time aggregate statistics and breakdown charts.
- **Implementation:**
  - `DashboardController.java` mapped to `/dashboard`.
  - Computes total papers count, count per status (`SUBMITTED`, `UNDER_REVIEW`, `ACCEPTED`, `REJECTED`, `REVISION_REQUESTED`), acceptance rate percentage, and recent submission logs.
- **Deliverables:** `DashboardController.java`, `dashboard.html`, MockMvc tests in `DashboardControllerTest.java`.
- **Status:** **COMPLETED** (5/5 dashboard tests passed)

#### Task 12: Input Validation, Error Handling & Comprehensive Test Suite
- **User Story:** US-010 (Quality Assurance & Validation)
- **Objective:** Provide uniform error pages and complete test suite coverage across unit, integration, and security layers.
- **Implementation:**
  - Created custom error templates: `403.html` (Access Denied), `404.html` (Not Found), `500.html` (Internal Error).
  - Expanded test suite to **76 standard automated tests** across 8 test classes:
    1. `RepositoryApplicationTests`: 1 test
    2. `PaperRepositoryTest`: 5 tests
    3. `PaperServiceTest`: 25 tests
    4. `HomeControllerTest`: 3 tests
    5. `PaperControllerTest`: 15 tests
    6. `ReviewControllerTest`: 10 tests
    7. `DashboardControllerTest`: 5 tests
    8. `SecurityConfigTest`: 12 tests
- **Deliverables:** Full automated test suite passing cleanly with zero failures.
- **Status:** **COMPLETED** (76/76 tests passed in 20.7s)

---

### Phase 3 & 4: DevOps Automation, CI/CD & Deployment (Weeks 8–15)

#### Task 13: Jenkins CI Declarative Pipeline
- **User Story:** US-011 (Continuous Integration Pipeline)
- **Objective:** Automate build verification, test execution, artifact creation, and test result archiving via Jenkins Declarative Pipeline.
- **Implementation:**
  - Created declarative [Jenkinsfile](file:///C:/Users/Muawiyah/Desktop/Jenkins-Based%20Research%20Paper%20Repository/Jenkinsfile) containing 6 distinct execution stages:
    1. `Checkout`: Checks out branch `*/main` from Git.
    2. `Verify Toolchain`: Displays Java and Maven versions.
    3. `Compile`: Runs `mvn clean compile` to ensure compilation integrity.
    4. `Test`: Executes unit and integration tests via `mvn test`.
    5. `Package`: Builds executable JAR `mvn package -DskipTests`.
    6. `Post-Build Actions`: Publishes JUnit XML test reports (`target/surefire-reports/*.xml`) and archives `target/*.jar`.
- **Status:** **COMPLETED** (Verified live with 3 successful consecutive builds)

#### Task 14: Automated E2E Smoke Tests with Selenium WebDriver
- **User Story:** US-012 (Automated Browser Journey Testing)
- **Objective:** Implement end-to-end browser smoke tests simulating real user interactions on a live application server.
- **Implementation:**
  - Implemented `PaperSubmissionUiTest.java` under Maven profile `-P e2e` using Selenium WebDriver 4.28.1.
  - Tests run using **Headless Chrome** with `--disable-gpu`, `--no-sandbox`, and `--disable-dev-shm-usage`:
    1. `testHomePageTitleAndNavigation`: Verifies landing page rendering and catalog links.
    2. `testCatalogPageLoadsAndRendersTable`: Verifies catalog search input and table headers.
    3. `testPublicCatalogDoesNotExposeSubmitterEmail`: Enforces security/privacy by confirming email headers and values are absent from the DOM.
- **Status:** **COMPLETED** (3/3 E2E tests passing)

#### Task 15: Containerization (Docker) & Infrastructure Provisioning (Ansible)
- **User Story:** US-013 (Container Packaging), US-014 (Automated Server Provisioning)
- **Objective:** Package the application into an immutable Docker container and provide an Ansible deployment playbook.
- **Implementation:**
  - `Dockerfile`: Multi-stage build using `maven:3.9.9-eclipse-temurin-21` for compilation and `eclipse-temurin:21-jre-jammy` for minimal non-root runtime image.
  - `docker-compose.yml`: Defines single-command stack with healthcheck (`/actuator/health` or `/`), port mapping `8081:8081`, and restart policies.
  - `ansible/inventory` & `ansible/deploy.yml`: Declarative playbook to install Docker prerequisites, pull repository, build container image, and run containerized service.
- **Status:** **COMPLETED** (Docker & Ansible files committed to `main`)

---

## 3. DevOps CI/CD Live Verification & Visual Proof of Work

### Part I: Terminal Commands Execution Proof

#### Figure 1: Maven Standard Test Suite Execution
Execution of `.\mvnw.cmd test` showing all 76 unit, slice, and security tests executing cleanly with `BUILD SUCCESS`:

![Maven Standard Test Suite Execution](evidence/cmd-01-mvnw-test.png)
*Figure 1: PowerShell Terminal Execution of Standard Test Suite (76/76 Tests Passed, 0 Failures, BUILD SUCCESS)*

---

#### Figure 2: Selenium E2E Headless Browser Test Execution
Execution of `.\mvnw.cmd test -P e2e -Dtest=PaperSubmissionUiTest` running headless Chrome browser tests:

![Selenium E2E Test Suite Execution](evidence/cmd-02-selenium-e2e.png)
*Figure 2: PowerShell Terminal Execution of Headless Chrome Selenium E2E Test Suite (3/3 Tests Passed, BUILD SUCCESS)*

---

#### Figure 3: Git Commit History & Branching Baseline
Execution of `git log --graph --oneline -n 12` demonstrating branch hygiene and conventional commit logs:

![Git Commit History and Branch Graph](evidence/cmd-03-git-log.png)
*Figure 3: PowerShell Terminal Execution of Git Branching & Commit Log*

---

#### Figure 4: Docker Container Packaging Command Verification
PowerShell command execution verifying the container build process via `Dockerfile`:

![Docker Build CLI Verification](evidence/docker-06-cli-build-verification.png)
*Figure 4: Terminal Execution of `docker build -t muawiyah123/research-paper-repository:latest .`*

---

### Part II: Jenkins Continuous Integration (CI) Live Evidence

#### Figure 5: Jenkins CI Welcome Dashboard
The Jenkins automation server running locally on port 8080 (`http://127.0.0.1:8080/`):

![Jenkins Dashboard Welcome](evidence/jenkins-01-dashboard-welcome.png)
*Figure 5: Jenkins Controller Running Locally on Port 8080*

---

#### Figures 6 & 7: Jenkins Pipeline SCM & Branch Configuration
The Pipeline was configured to pull from Git SCM targeting branch `*/main` and `Jenkinsfile`:

![Jenkins Pipeline Configuration](evidence/jenkins-02-pipeline-configuration.png)
*Figure 6: Jenkins Job Configuration targeting Git SCM and `Jenkinsfile`*

![Jenkins Branch Specifier](evidence/jenkins-03-branch-specifier.png)
*Figure 7: Jenkins SCM Branch Specifier Configuration*

---

#### Figure 8: Jenkins Pipeline Build #1 Execution Success
The pipeline executed all 6 stages synchronously with **SUCCESS** status:
- **Build Number:** Build #1 (14 Oct 2026, 14:05:53)
- **Author:** `Muawiyah Sahil Narvel`
- **Revision:** `2e7188dace066724924c35074021bab940cf14d4`
- **Build Artifact:** `repository-0.0.1-SNAPSHOT.jar` (53.44 MiB)
- **Test Results:** Tests (no failures)

![Jenkins Build #1 Success Details](evidence/jenkins-04-build1-success-details.png)
*Figure 8: Jenkins Build #1 Detailed Execution View — 100% Successful Build & Packaged JAR Artifact*

---

#### Figure 9: Multi-Build Trend & Automated Test Growth
Jenkins **Test Result Trend** graph reflecting 3 consecutive successful builds (#1, #2, #3):
- **Build #1:** 28 tests passed (0 failed, 0 skipped)
- **Build #2:** 36 tests passed (0 failed, 0 skipped)
- **Build #3:** 39 tests passed (0 failed, 0 skipped)
- **Packaged JAR:** `repository-0.0.1-SNAPSHOT.jar` (57.06 MiB)

![Jenkins Test Result Trend](evidence/jenkins-05-build-trend-test-results.png)
*Figure 9: Jenkins Test Result Trend Chart — 3 Consecutive Green Builds with Continuous Test Growth*

---

### Part III: Web Application User Interface Proof of Work

#### Figure 10: Public Home & Navigation Portal (Task 4)
The public landing page with candidate attribution banner (`Muawiyah Narvel, 23108A0020`) and platform foundation baseline:

![Public Home Portal](evidence/ui-01-homepage.png)
*Figure 10: Task 4 — Public Landing Page (`http://localhost:8081/`)*

---

#### Figure 11: Public Research Paper Catalog & Keyword Search (Task 7)
Real-time keyword search with strict submitter email redaction to protect researcher privacy:

![Public Catalog Page](evidence/ui-02-catalog.png)
*Figure 11: Task 7 — Public Catalog & Search (`http://localhost:8081/papers`)*

---

#### Figure 12: In-Memory Spring Security 6 Sign-In Portal (Task 9)
Custom sign-in card with Academic Development Accounts helper card:

![Login Page](evidence/ui-03-login.png)
*Figure 12: Task 9 — Spring Security Sign-In Portal (`http://localhost:8081/login`)*

---

#### Figure 13: Research Paper Submission Form (Task 6)
Authenticated submission form displaying input validation boundaries under `ROLE_RESEARCHER`:

![Submission Form](evidence/ui-04-submission-form.png)
*Figure 13: Task 6 — Paper Submission Form (`http://localhost:8081/papers/submit`)*

---

#### Figure 14: Peer-Review Workflow & 5-State Lifecycle Queue (Task 10)
Reviewer evaluation queue with status filters (Submitted, Under Review, Revision Requested, Accepted, Rejected) and Evaluate action buttons:

![Review Portal](evidence/ui-05-review-queue.png)
*Figure 14: Task 10 — Peer-Review Lifecycle Management Queue (`http://localhost:8081/papers/review`)*

---

#### Figure 15: Administrative Metrics Dashboard (Task 11)
Real-time repository analytics with aggregate KPI status cards and recent submission logs:

![Admin Dashboard](evidence/ui-06-admin-dashboard.png)
*Figure 15: Task 11 — Administrative Metrics Dashboard (`http://localhost:8081/dashboard`)*

---

## 4. Test Suite Summary & Quality Metrics

| Test Category | Test Class | Framework | Test Count | Status |
|---|---|---|---|---|
| **Context Bootstrap** | `RepositoryApplicationTests` | Spring Boot Test | 1 | PASS |
| **JPA Repository** | `PaperRepositoryTest` | DataJpaTest / H2 | 5 | PASS |
| **Service & State Machine** | `PaperServiceTest` | JUnit 5 / Mockito | 25 | PASS |
| **Public Web Slice** | `HomeControllerTest` | MockMvc | 3 | PASS |
| **Submission & Catalog Slice**| `PaperControllerTest` | MockMvc | 15 | PASS |
| **Review Workflow Slice** | `ReviewControllerTest` | MockMvc | 10 | PASS |
| **Admin Analytics Slice** | `DashboardControllerTest` | MockMvc | 5 | PASS |
| **Security & RBAC Slice** | `SecurityConfigTest` | Spring Security MockMvc | 12 | PASS |
| **E2E Smoke Testing** | `PaperSubmissionUiTest` | Selenium Headless Chrome | 3 | PASS |
| **Total Test Suite** | **9 Test Classes** | **JUnit 5 / MockMvc / Selenium** | **79 Tests** | **100% PASS** |

---

## 5. Conclusion & Academic Compliance

The **Jenkins-Based Research Paper Repository** project successfully fulfills all requirements outlined in the 15-task development plan. 

Key milestones achieved:
1. **Full Academic Lifecycle:** Seamless paper submission, keyword search, metadata editing, peer-review state machine, and administrative dashboard.
2. **Security & Privacy by Design:** Role-based access control with BCrypt password hashing, CSRF enforcement, and strict submitter email redaction on public endpoints.
3. **Automated CI/CD Pipeline:** Fully functional Jenkins pipeline with automated compile, test, package, and JUnit report publishing.
4. **End-to-End Quality Assurance:** 79 automated tests validating the system from unit logic up to live browser interactions.
5. **Production Readiness:** Containerization via multi-stage Dockerfile and declarative Ansible playbook for automated deployments.

---

*Report submitted by:*  
**Muawiyah Sahil Narvel**  
Roll Number: `23108A0020`  
Date: October 15, 2026
