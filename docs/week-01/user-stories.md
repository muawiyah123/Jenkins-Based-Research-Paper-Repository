# User Stories & Acceptance Criteria
## Jenkins-Based Research Paper Repository

**Student Name:** Muawiyah Narvel  
**Roll Number:** 23108A0020  
**Project Title:** Jenkins-Based Research Paper Repository  
**Document Reference:** `docs/week-01/user-stories.md`  
**Status:** Approved Baseline  

---

## 1. Overview & Phased Implementation Schedule
This backlog details all 17 user stories across 15 organizational epics for the Jenkins-Based Research Paper Repository. 

### Phased Project Roadmap
- **Weeks 1–3 (Foundation Milestone):** Requirements specification, architecture design, development environment verification, minimal Spring Boot project scaffold, responsive home page, Maven build verification, and clean Git repository initialization (`US-001`, `US-011`, `US-012`).
- **Weeks 4–7 (Application MVP Implementation):** Security configuration, database entity persistence, CRUD workflows, public search, review state machine, dashboard metrics, and error handling (`US-002` through `US-010`).
- **Weeks 8–15 (DevOps Pipeline & Final Verification):** Automated Jenkins CI pipeline, Selenium browser automation, Docker containerization, Ansible configuration management, and final viva portfolio preparation (`US-013` through `US-017`).

*Note: All timeline schedules and dates represent provisional academic planning targets, not rigid calendar deadlines.*

---

## 2. Detailed User Stories by Epic

### EPIC 1: Project Setup and Home Page

#### US-001: Initial Home Page Display
- **Epic:** EPIC 1
- **Story Title:** Initial Home Page Display
- **Role:** Public Visitor
- **User Story:** As a visitor, I want to access a responsive home page at the root URL (`/`), so that I understand the repository's purpose and can navigate to public catalogs.
- **Linked SRS:** NFR-02, NFR-05, NFR-06
- **Priority:** Must
- **Scope:** MVP (Weeks 1–3 Foundation Milestone)
- **Dependencies:** Spring Boot project scaffold, Thymeleaf, Bootstrap 5
- **Estimated Effort:** Small
- **Acceptance Criteria:**
  - `AC-001-1`: Given the application is running on port 8080, when a visitor navigates to `http://localhost:8080/`, then the server returns HTTP 200 and renders the home page with the title "Research Paper Repository".
  - `AC-001-2`: Given the home page renders, when inspected, then visible navigation links exist for "Home", "Browse Papers", "Submit Paper", and "Dashboard". Navigation links to features not yet implemented during Week 3 must remain inert or point to safe placeholder views, and must not accidentally expose unbuilt protected routes.
  - `AC-001-3 (Provisional Latency Target)`: Under single-user localhost development conditions, requests to `GET /` typically respond within 500 ms as measured by browser DevTools or curl. *(Note: This is a provisional design target that depends on the local environment and remains unverified until empirically measured in Week 3).*
- **Positive Test Scenarios:** Request `GET /` returns HTTP 200 with styled responsive layout.
- **Negative Test Scenarios:** Requesting an invalid static asset (e.g., `GET /missing.css`) returns standard HTTP 404 without crashing.
- **Proposed Verification Method:** Spring Boot MockMvc test `mockMvc.perform(get("/")).andExpect(status().isOk())` and manual browser inspection.
- **Required Evidence (Planned):** Terminal application launch log and browser screenshot (`week-03-home-page.png`).
- **Definition of Done:** Home page controller and Thymeleaf template exist, build compiles, test passes.

---

### EPIC 2: Research Paper Data Model and Persistence

#### US-002: Paper Entity Relational Persistence
- **Epic:** EPIC 2
- **Story Title:** Paper Entity Relational Persistence
- **Role:** System Developer
- **User Story:** As a developer, I want paper metadata mapped via Spring Data JPA to an embedded H2 database, so that paper records are persisted transactionally during application execution.
- **Linked SRS:** FR-08, NFR-04
- **Priority:** Must
- **Scope:** Application MVP (Weeks 4–7)
- **Dependencies:** US-001, H2 dependency, Spring Data JPA
- **Estimated Effort:** Medium
- **Acceptance Criteria:**
  - `AC-002-1`: Given the Paper entity, when the application starts, then Spring Data JPA creates an H2 table matching the schema (`id`, `title`, `authors`, `abstract_text`, `keywords`, `publication_year`, `doi_url`, `submitter_email`, `submitter_username`, `review_status`, `created_at`, `updated_at`).
  - `AC-002-2`: Given a valid Paper entity instance, when `paperRepository.save(paper)` is executed, then an auto-generated primary key ID is assigned and creation timestamps are populated.
- **Positive Test Scenarios:** Persist a valid paper and retrieve it by ID with matching field values.
- **Negative Test Scenarios:** Attempting to persist with null non-nullable fields (e.g., null title) throws a `DataIntegrityViolationException`.
- **Proposed Verification Method:** `@DataJpaTest` integration test using JUnit 5.
- **Required Evidence (Planned):** Maven Surefire test report verifying JPA repository save and retrieve methods.
- **Definition of Done:** Entity class, repository interface, and repository test written and passing.

---

### EPIC 3: Create, List, View and Edit Paper Records

#### US-003: Public Catalog Listing and Paper Details
- **Epic:** EPIC 3
- **Story Title:** Public Catalog Listing and Paper Details
- **Role:** Public / Unauthenticated Visitor
- **User Story:** As a public visitor, I want to view a table of registered research papers and open individual paper details without logging in, so that I can explore institutional scholarship freely while submitter private contacts remain protected.
- **Linked SRS:** FR-02, FR-07
- **Priority:** Must
- **Scope:** Application MVP (Weeks 4–7)
- **Dependencies:** US-002
- **Estimated Effort:** Medium
- **Acceptance Criteria:**
  - `AC-003-1`: Given papers exist in the database, when an unauthenticated visitor accesses `GET /papers`, then an HTML table displays all papers showing title, authors, publication year, and review status badge with HTTP 200 without requiring login.
  - `AC-003-2`: Given a paper with ID `1` exists, when an unauthenticated visitor accesses `GET /papers/1`, then a detail page displays the complete title, authors, abstract, keywords, publication year, and DOI (if present) without requiring login.
  - `AC-003-3 (Privacy Guard)`: Given the public catalog and detail pages render, then `submitterEmail` or other private submitter contact details are **never displayed** in the public HTML DOM to protect submitter contact privacy.
  - `AC-003-4`: Given an unauthenticated visitor requests `GET /papers/99999` (non-existent ID), then a styled HTTP 404 page is returned rather than an unhandled exception or stack trace.
  - `AC-003-5 (Provisional Latency Target)`: Under single-user localhost development conditions, requests to `GET /papers` typically respond within 500 ms as measured by browser DevTools or curl. *(Note: This is a provisional design target that depends on the local environment and is not claimed as achieved until empirically measured in Week 5).*
- **Positive Test Scenarios:** Public browsing of catalog and detailed abstracts without login prompts.
- **Negative Test Scenarios:** Querying non-existent paper returns clean 404 page.
- **Proposed Verification Method:** Spring MVC `MockMvc` integration test asserting HTTP 200, model contents, and absence of email in rendered HTML.
- **Required Evidence (Planned):** Terminal test output and browser screenshots of catalog and public detail view.
- **Definition of Done:** Controller routes `/papers` and `/papers/{id}` return correct Thymeleaf templates with privacy controls active.

#### US-004: Create Research Paper Record
- **Epic:** EPIC 3
- **Story Title:** Create Research Paper Record
- **Role:** Researcher
- **User Story:** As an authenticated Researcher, I want to submit a paper metadata form, so that my paper is registered with an initial status of `SUBMITTED` and attributed to my authenticated account identity.
- **Linked SRS:** FR-01, FR-07, FR-08
- **Priority:** Must
- **Scope:** Application MVP (Weeks 4–7)
- **Dependencies:** US-002, US-003, US-009
- **Estimated Effort:** Medium
- **Acceptance Criteria:**
  - `AC-004-1`: Given an authenticated user with `ROLE_RESEARCHER` (e.g. development account `researcher`), when they submit valid form data to `POST /papers`, then the record is saved with `reviewStatus = SUBMITTED`, the submitter identity (`submitterUsername = "researcher"`) is derived server-side from `Authentication.getName()` as the sole source of paper ownership (ignoring client-supplied identity fields), `submitterEmail` is stored strictly as contact metadata, and the browser redirects to `/papers` with a success alert.
  - `AC-004-2`: Given an unauthenticated visitor, when they attempt `GET /papers/new` or `POST /papers`, then the system redirects them to `/login`.
- **Positive Test Scenarios:** Valid form submission saves paper to H2 and displays in list with server-derived ownership.
- **Negative Test Scenarios:** Submitting invalid form (e.g. title < 5 chars) redisplays form with inline errors and does not persist record.
- **Proposed Verification Method:** MockMvc test with `@WithMockUser(username = "researcher", roles = "RESEARCHER")`.
- **Required Evidence (Planned):** Form submission test execution log and browser screenshot of completed submission.
- **Definition of Done:** Form controller, template, and validation binding completed and tested.

#### US-005: Edit Paper Metadata (Author Only)
- **Epic:** EPIC 3
- **Story Title:** Edit Paper Metadata (Author Only)
- **Role:** Researcher
- **User Story:** As an authenticated Researcher, I want to edit metadata for my own paper while in `SUBMITTED` or `REVISION_REQUESTED` status, so that I can correct errors or incorporate reviewer feedback and resubmit.
- **Linked SRS:** FR-03, FR-07
- **Priority:** Must
- **Scope:** Application MVP (Weeks 4–7)
- **Dependencies:** US-004, US-009
- **Estimated Effort:** Medium
- **Acceptance Criteria:**
  - `AC-005-1`: Given a paper in `SUBMITTED` status authored by the logged-in Researcher (matching `Authentication.getName().equals(paper.getSubmitterUsername())`), when updated metadata is submitted to `POST /papers/{id}/edit`, then the database record is updated and `updatedAt` is refreshed.
  - `AC-005-2 (Resubmission)`: Given a paper in `REVISION_REQUESTED` status authored by the logged-in Researcher, when the author updates metadata and submits the edit form, then the server verifies ownership against the authenticated principal (`submitterUsername`), updates the metadata in H2, and automatically transitions `reviewStatus` from `REVISION_REQUESTED` to `UNDER_REVIEW`.
  - `AC-005-3`: Given a paper in `UNDER_REVIEW`, `ACCEPTED`, or `REJECTED`, when the author attempts to access the edit form or post updates, then the server rejects the request with HTTP 400 or HTTP 403.
  - `AC-005-4`: Given a paper submitted by `researcher`, when a different authenticated account (or unauthenticated user) attempts to access `GET /papers/{id}/edit` or post updates, then the server rejects the request with HTTP 403 Forbidden.
  - `AC-005-5`: Given an authenticated Reviewer (e.g. `reviewer`), when they attempt to edit paper metadata (title, abstract, etc.), then the server blocks the request (Reviewers only manage review status).
- **Positive Test Scenarios:** Author updates title and abstract of own `SUBMITTED` paper successfully; author resubmits `REVISION_REQUESTED` paper transitioning to `UNDER_REVIEW`.
- **Negative Test Scenarios:** Non-author, reviewer, or author of `ACCEPTED` paper blocked server-side from saving changes.
- **Proposed Verification Method:** Service unit tests and MockMvc security tests with multiple mock user principals.
- **Required Evidence (Planned):** Test execution log verifying HTTP 403 on unauthorized edit attempts.
- **Definition of Done:** Authorization checks in service/controller verified with unit and integration tests.

---

### EPIC 4: Search and Filtering

#### US-006: Public Multi-Attribute Search
- **Epic:** EPIC 4
- **Story Title:** Public Multi-Attribute Search
- **Role:** Public / Unauthenticated Visitor
- **User Story:** As a public visitor, I want to search papers case-insensitively across title, author, and keyword fields, so that I can discover relevant research papers easily without logging in.
- **Linked SRS:** FR-04, FR-07
- **Priority:** Must
- **Scope:** Application MVP (Weeks 4–7)
- **Dependencies:** US-002, US-003
- **Estimated Effort:** Medium
- **Acceptance Criteria:**
  - `AC-006-1 (Title Match)`: Given papers exist titled "Spring Boot CI/CD" and "Database Systems", when a visitor searches for "spring" (lowercase), then "Spring Boot CI/CD" is returned (case-insensitive title match).
  - `AC-006-2 (Author Match)`: Given a paper with author "Narvel", when a visitor searches for "narvel", then the paper is returned (case-insensitive author match).
  - `AC-006-3 (Keyword Match)`: Given a paper with keyword "DevOps", when a visitor searches for "devops", then the paper is returned (case-insensitive keyword match).
  - `AC-006-4 (Empty Query)`: Given a visitor submits an empty or whitespace-only search string, then all registered papers are returned without query errors.
  - `AC-006-5 (No Results)`: Given a query with no matches (e.g. "QuantumComputingXYZ"), then an empty results table is rendered with a friendly message: "No papers found matching your query."
  - `AC-006-6 (Unauthenticated)`: All search operations execute without requiring login.
- **Positive Test Scenarios:** Partial, mixed-case queries match title, author, or keyword.
- **Negative Test Scenarios:** Unmatched query displays clear feedback without exception.
- **Proposed Verification Method:** JPA repository integration test and Spring MVC MockMvc search query test.
- **Required Evidence (Planned):** Search query test output and browser screenshot of search results and no-results states.
- **Definition of Done:** Repository custom query method and search view implemented and tested.

---

### EPIC 5: Review Workflow and Status Transitions

#### US-007: Review Status State Machine & Management
- **Epic:** EPIC 5
- **Story Title:** Review Status State Machine & Management
- **Role:** Reviewer & Researcher
- **User Story:** As a Reviewer, I want to advance papers through formal review stages, and as a Researcher, I want to resubmit papers requiring revision, so that the academic peer-review lifecycle is enforced and invalid transitions are rejected.
- **Linked SRS:** FR-05, FR-07
- **Priority:** Must
- **Scope:** Application MVP (Weeks 4–7)
- **Dependencies:** US-002, US-004, US-009
- **Estimated Effort:** Large
- **Acceptance Criteria:**
  - `AC-007-1 (Permitted Reviewer Transitions)`: Given an authenticated Reviewer (`ROLE_REVIEWER`):
    - Moving `SUBMITTED` → `UNDER_REVIEW` succeeds.
    - Moving `UNDER_REVIEW` → `ACCEPTED` succeeds (terminal state).
    - Moving `UNDER_REVIEW` → `REJECTED` succeeds (terminal state).
    - Moving `UNDER_REVIEW` → `REVISION_REQUESTED` succeeds.
  - `AC-007-2 (Researcher Resubmission)`: Given a paper in `REVISION_REQUESTED`, when the original author submits updated metadata, then the review status automatically transitions to `UNDER_REVIEW`.
  - `AC-007-3 (Forbidden Forward Jumps)`: Given a paper in `SUBMITTED`, when any user attempts to transition directly to `ACCEPTED` or `REVISION_REQUESTED`, then the server rejects the request with HTTP 400 Bad Request.
  - `AC-007-4 (Immutable Terminal States)`: Given a paper in `ACCEPTED` or `REJECTED`, when any transition is attempted, then the server rejects it with HTTP 400 Bad Request and the status remains unchanged.
  - `AC-007-5 (Role Enforcement)`: Given an unauthenticated visitor or a user with `ROLE_RESEARCHER`, when they attempt to invoke reviewer transition endpoints (`POST /papers/{id}/status`), then the server returns HTTP 403 Forbidden.
- **Positive Test Scenarios:** All 5 permitted lifecycle transitions execute and persist correctly.
- **Negative Test Scenarios:** Out-of-order transitions, terminal-state modification attempts, and unauthorized role requests fail server-side.
- **Proposed Verification Method:** Isolated JUnit 5 domain service tests testing every transition pair, plus Spring Security MockMvc integration tests.
- **Required Evidence (Planned):** Target JUnit test report verifying all state transition test cases upon execution.
- **Definition of Done:** Review workflow service and status controller completed with unit test coverage.

---

### EPIC 6: Dashboard and Repository Statistics

#### US-008: Repository Metrics Dashboard
- **Epic:** EPIC 6
- **Story Title:** Repository Metrics Dashboard
- **Role:** Authenticated User (`ROLE_RESEARCHER`, `ROLE_REVIEWER`, `ROLE_ADMIN`)
- **User Story:** As an authenticated user, I want to access a dashboard displaying paper counts for every review status plus total papers, so that I can monitor repository throughput.
- **Linked SRS:** FR-06, FR-07
- **Priority:** Must
- **Scope:** Application MVP (Weeks 4–7)
- **Dependencies:** US-002, US-007, US-009
- **Estimated Effort:** Small
- **Acceptance Criteria:**
  - `AC-008-1`: Given an authenticated user with any valid role, when they navigate to `GET /dashboard`, then the page renders with HTTP 200 displaying dedicated count cards for: Total Papers, Submitted, Under Review, Revision Requested, Accepted, and Rejected.
  - `AC-008-2`: Given 1 submitted, 2 under review, 1 revision requested, 3 accepted, and 1 rejected paper in H2, then the dashboard cards render exact values: Total: 8, Submitted: 1, Under Review: 2, Revision Requested: 1, Accepted: 3, Rejected: 1.
  - `AC-008-3`: Given an unauthenticated visitor accesses `GET /dashboard`, then they are redirected to `/login` with HTTP 302.
- **Positive Test Scenarios:** Accurate status counts derived via database aggregation queries.
- **Negative Test Scenarios:** Unauthenticated access blocked.
- **Proposed Verification Method:** Spring MVC MockMvc test and repository aggregation test.
- **Required Evidence (Planned):** Dashboard test execution log and browser screenshot of metric cards upon implementation.
- **Definition of Done:** Aggregation service, controller, and Thymeleaf template created and tested.

---

### EPIC 7: Authentication and Server-Side Authorization

#### US-009: In-Memory Development Authentication & Role Guards
- **Epic:** EPIC 7
- **Story Title:** In-Memory Development Authentication & Role Guards
- **Role:** Security Officer / Developer
- **User Story:** As a developer, I want Spring Security configured with mock in-memory user credentials, so that backend authorization rules can be rigorously tested without complex user-management overhead.
- **Linked SRS:** FR-07, NFR-07
- **Priority:** Must
- **Scope:** Application MVP (Weeks 4–7 Foundation for Protected Endpoints)
- **Dependencies:** US-001
- **Estimated Effort:** Medium
- **Acceptance Criteria:**
  - `AC-009-1`: Given three development accounts (`researcher`, `reviewer`, `admin`) configured in Spring Security, when `researcher` logs in, then they obtain authority `ROLE_RESEARCHER`.
  - `AC-009-2`: Given unauthenticated requests to `/`, `/papers`, `/papers/*`, and `/papers/search`, then access is granted with HTTP 200 without login challenges.
  - `AC-009-3`: Given a user logged in as `researcher`, when they attempt to post to Reviewer status endpoints (`POST /papers/{id}/status`), then Spring Security responds with HTTP 403 Forbidden.
  - `AC-009-4 (Ownership Attribution)`: Given a paper submitted while authenticated as `researcher`, the server sets `submitterUsername = "researcher"` derived from `Authentication.getName()`. For all author editing (`FR-03`) and resubmission (`FR-05`) operations, the server compares `Authentication.getName()` with `paper.getSubmitterUsername()`. The `submitterEmail` field is maintained strictly as author contact metadata and does not confer ownership.
- **Positive Test Scenarios:** Successful authentication for each role granting intended permissions.
- **Negative Test Scenarios:** Bad password attempt fails with standard error; unauthorized role endpoint returns 403.
- **Proposed Verification Method:** Spring Security MockMvc integration tests using `@WithMockUser`.
- **Required Evidence (Planned):** Security configuration file and passing security test suite logs.
- **Definition of Done:** `SecurityConfig` class created, routes secured, and security tests pass.

---

### EPIC 8: Validation and Error Handling

#### US-010: Input Validation & User-Friendly Error Pages
- **Epic:** EPIC 8
- **Story Title:** Input Validation & User-Friendly Error Pages
- **Role:** Researcher / Public Visitor
- **User Story:** As a user, I want immediate field-level validation feedback and styled error pages, so that invalid input is caught gracefully without confusing server errors.
- **Linked SRS:** FR-01, FR-03, NFR-06
- **Priority:** Must
- **Scope:** Application MVP (Weeks 4–7)
- **Dependencies:** US-004, US-005
- **Estimated Effort:** Small
- **Acceptance Criteria:**
  - `AC-010-1`: Given a user submits a paper form with a 3-character title or year 1850, then the form redisplays with inline red validation error messages.
  - `AC-010-2`: Given a user submits an email without an `@` sign, then validation fails with "Please provide a valid submitter email address".
  - `AC-010-3`: Given any request for a non-existent URL or entity ID, then a custom styled `404.html` error template renders with a "Return to Catalog" link.
- **Positive Test Scenarios:** Clean inline feedback on invalid inputs; seamless navigation from 404 page.
- **Negative Test Scenarios:** No unhandled 500 stack traces leaked to browser.
- **Proposed Verification Method:** MockMvc form validation test and custom exception handler test.
- **Required Evidence (Planned):** Browser screenshot of form with validation errors and 404 page.
- **Definition of Done:** Validation annotations (`@NotBlank`, `@Size`, `@Min`, `@Max`, `@Email`) attached to DTO and GlobalExceptionHandler active.

---

### EPIC 9: Git and GitHub Workflow

#### US-011: Git Version Control Initialization and Clean History
- **Epic:** EPIC 9
- **Story Title:** Git Version Control Initialization and Clean History
- **Role:** Student Developer
- **User Story:** As a developer, I want a cleanly initialized Git repository with `.gitignore` and conventional commit messages, so that all project milestones are auditable and protected against accidental secret leakage.
- **Linked SRS:** NFR-01, NFR-07
- **Priority:** Must
- **Scope:** MVP (Weeks 1–3 Foundation Milestone)
- **Dependencies:** US-001
- **Estimated Effort:** Small
- **Acceptance Criteria:**
  - `AC-011-1`: Given the project directory, when `git status` is executed after initial setup, then `.gitignore` excludes `target/`, `.idea/`, `.vscode/`, `.env`, and `*.log` files.
  - `AC-011-2`: Given the initial commit on `main`, when `git log -1 --oneline` is run, then a conventional commit message (e.g. `chore: initialize Spring Boot project`) is recorded.
  - `AC-011-3 (Secret Hygiene Verification)`: Systematic verification inspects tracked files (`git ls-files`), verifies `.gitignore` exclusion of secret and local config files, and audits commit history (`git log`) to verify zero passwords, private keys, API tokens, or personal roll numbers are committed, without printing or leaking secret values in verification logs or evidence artifacts.
- **Positive Test Scenarios:** Clean Git status, single initial commit, valid `.gitignore`.
- **Negative Test Scenarios:** Attempting to stage `target/` build artifacts is prevented by `.gitignore`.
- **Proposed Verification Method:** PowerShell commands `git status`, `git ls-files`, and `git log`.
- **Required Evidence (Planned):** Terminal output log (`week-03-git-commit.txt`).
- **Definition of Done:** Git initialized on branch `main`, verified clean working tree.

---

### EPIC 10: Maven Build and Automated Application Tests

#### US-012: Maven Build Automation & Test Pipeline
- **Epic:** EPIC 10
- **Story Title:** Maven Build Automation & Test Pipeline
- **Role:** Student Developer / CI Server
- **User Story:** As a developer, I want to compile, test, and package the application using `./mvnw`, so that build reproducibility is guaranteed on any operating system without global Maven dependencies.
- **Linked SRS:** NFR-01, NFR-02, NFR-04
- **Priority:** Must
- **Scope:** MVP (Weeks 1–3 Foundation Milestone)
- **Dependencies:** US-001
- **Estimated Effort:** Small
- **Acceptance Criteria:**
  - `AC-012-1 (Week 3 Acceptance Gate)`: Given Java 21 is active, when `mvnw.cmd clean test` is run on Windows during Week 3, then the build compiles and executes the standard Spring Boot context-load test (`contextLoads()`) with 0 failures and 0 errors, and `mvnw.cmd package` produces an executable JAR. *(Note: Domain business logic, CRUD workflows, review status transitions, and JPA persistence tests are planned for implementation and verification in Weeks 4–7).*
  - `AC-012-2`: Given `mvnw.cmd package` is executed, then an executable JAR file is produced under `target/research-paper-repository-*.jar`.
- **Positive Test Scenarios:** Clean compilation, test pass, and JAR creation.
- **Negative Test Scenarios:** Introducing a deliberate test failure causes `mvnw.cmd test` to exit with a non-zero exit code and fail the build.
- **Proposed Verification Method:** Running `mvnw.cmd clean package` from command line.
- **Required Evidence (Planned):** Terminal output showing `BUILD SUCCESS` and Surefire test report (`week-03-maven-test-output.txt`).
- **Definition of Done:** Maven wrapper functional, JAR builds and runs cleanly via `java -jar`.

---

### EPIC 11: Jenkins Continuous Integration (Planned)

#### US-013: Jenkins Declarative Pipeline Automation
- **Epic:** EPIC 11
- **Story Title:** Jenkins Declarative Pipeline Automation
- **Role:** DevOps Engineer
- **User Story:** As a DevOps engineer, I want a declarative `Jenkinsfile` in source control, so that every push to GitHub triggers an automated build, test, and package pipeline.
- **Linked SRS:** NFR-03
- **Priority:** Should
- **Scope:** Planned (Weeks 8–10 DevOps Milestone)
- **Dependencies:** US-011, US-012, Jenkins server setup
- **Estimated Effort:** Medium
- **Acceptance Criteria:**
  - `AC-013-1`: Given code is pushed to Git, when Jenkins triggers, then stages "Checkout", "Compile", "Test", and "Package" execute in sequence.
  - `AC-013-2`: Given any unit test fails during the "Test" stage, then the Jenkins build terminates as `FAILED`, no artifacts are published, and failure notifications appear in the console.
- **Positive Test Scenarios:** Successful pipeline execution with green status and JUnit test report published.
- **Negative Test Scenarios:** Broken build stops pipeline at the failing stage.
- **Proposed Verification Method:** Jenkins pipeline execution log and stage view dashboard.
- **Required Evidence (Planned):** Screenshot of successful Jenkins pipeline stage view.
- **Definition of Done:** `Jenkinsfile` committed, tested against local Jenkins instance.

---

### EPIC 12: Selenium Browser Testing (Planned)

#### US-014: Automated End-to-End UI Smoke Tests
- **Epic:** EPIC 12
- **Story Title:** Automated End-to-End UI Smoke Tests
- **Role:** QA Engineer
- **User Story:** As a QA engineer, I want Selenium WebDriver automated browser tests, so that critical user workflows (catalog browsing, paper submission) are verified in a real browser environment.
- **Linked SRS:** NFR-04, NFR-06
- **Priority:** Should
- **Scope:** Planned (Weeks 10–11 DevOps Milestone)
- **Dependencies:** US-001, US-003, US-004
- **Estimated Effort:** Medium
- **Acceptance Criteria:**
  - `AC-014-1`: Given the application is running, when the Selenium test suite runs against `http://localhost:8080`, then the browser opens, navigates through the home page to the paper form, fills in data, submits, and asserts that the new title is displayed on the catalog page.
  - `AC-014-2`: Given a validation error scenario, when Selenium inputs an invalid year, then it asserts that the expected red error message element is present in the DOM.
- **Positive Test Scenarios:** Headless browser automation completes user flow successfully.
- **Negative Test Scenarios:** UI element locator failure correctly fails the test suite.
- **Proposed Verification Method:** Maven test profile executing Selenium WebDriver suite.
- **Required Evidence (Planned):** Selenium test execution report and headless browser execution logs.
- **Definition of Done:** Selenium test classes created in `src/test/java`, passing headless run.

---

### EPIC 13: Docker Image and Deployment (Planned)

#### US-015: Multi-Stage Container Packaging and Health Verification
- **Epic:** EPIC 13
- **Story Title:** Multi-Stage Container Packaging and Health Verification
- **Role:** DevOps Engineer
- **User Story:** As a DevOps engineer, I want a multi-stage `Dockerfile`, so that the application is packaged into a lightweight, self-contained container image capable of running anywhere.
- **Linked SRS:** NFR-01, NFR-02
- **Priority:** Should
- **Scope:** Planned (Weeks 11–12 DevOps Milestone)
- **Dependencies:** US-012, Docker Desktop installed
- **Estimated Effort:** Medium
- **Acceptance Criteria:**
  - `AC-015-1`: Given the repository source, when `docker build -t research-paper-repo:latest .` is executed, then a multi-stage build compiles with Maven and produces a runnable JRE 21 container image.
  - `AC-015-2`: Given the container is launched with `docker run -p 8080:8080`, when curled, then `http://localhost:8080/` responds with HTTP 200 within 15 seconds.
- **Positive Test Scenarios:** Image builds cleanly, container starts and serves traffic on port 8080.
- **Negative Test Scenarios:** Unhealthy container fails container health check probe.
- **Proposed Verification Method:** Terminal command `docker build` and `docker run` followed by `curl http://localhost:8080`.
- **Required Evidence (Planned):** `docker images` and `docker ps` console output.
- **Definition of Done:** Multi-stage `Dockerfile` and `.dockerignore` committed and verified.

---

### EPIC 14: Ansible Configuration Management (Planned)

#### US-016: Automated Host Provisioning and Playbook Execution
- **Epic:** EPIC 14
- **Story Title:** Automated Host Provisioning and Playbook Execution
- **Role:** Infrastructure Engineer
- **User Story:** As an infrastructure engineer, I want Ansible playbooks to provision prerequisites and deploy the application container, so that server deployment is repeatable and idempotent.
- **Linked SRS:** NFR-01
- **Priority:** Could
- **Scope:** Planned (Weeks 12–13 DevOps Milestone)
- **Dependencies:** US-015, Target host environment (local VM, secondary physical machine, or container host subject to faculty clarification and resource availability)
- **Estimated Effort:** Medium
- **Acceptance Criteria:**
  - `AC-016-1`: Given an Ansible inventory file, when `ansible-playbook -i inventory deploy.yml` is executed, then Docker and dependencies are installed, and the application container is deployed and started.
  - `AC-016-2`: Given the playbook is executed a second time against an already-deployed target, then Ansible reports `changed=0`, proving idempotency.
- **Positive Test Scenarios:** Single playbook command provisions and verifies server.
- **Negative Test Scenarios:** Target host unreachable fails fast with descriptive SSH/connection failure.
- **Proposed Verification Method:** `ansible-playbook` run with idempotency verification check.
- **Required Evidence (Planned):** Ansible terminal execution summary showing `ok` and `changed=0`.
- **Definition of Done:** Playbook and inventory files stored under `ansible/` directory.

---

### EPIC 15: Final Verification, Documentation and Viva (Planned)

#### US-017: Comprehensive Evidence Portfolio and Viva Demonstration
- **Epic:** EPIC 15
- **Story Title:** Comprehensive Evidence Portfolio and Viva Demonstration
- **Role:** Student Candidate
- **User Story:** As a graduating student, I want an organized evidence portfolio, clean documentation, and automated demo scripts, so that I can confidently present the full engineering lifecycle during project viva.
- **Linked SRS:** All Requirements
- **Priority:** Should
- **Scope:** Planned (Weeks 14–15 Final Milestone)
- **Dependencies:** All previous epics
- **Estimated Effort:** Medium
- **Acceptance Criteria:**
  - `AC-017-1`: Given all weeks of work, when audited, then every milestone has an accompanying genuine screenshot or log file in `evidence/`.
  - `AC-017-2 (Final Verification Target)`: Given an evaluator asks for a live build and test demonstration, the project target is for `./mvnw clean verify` to execute all unit, integration, and UI tests with 100% success without unexpected failures. *Note: This represents a final verification milestone target upon project completion, not a current result or guaranteed outcome prior to implementation.*
- **Positive Test Scenarios:** Complete documentation matching actual repository code and working live demonstration.
- **Negative Test Scenarios:** No unverified claims or phantom features present in final report.
- **Proposed Verification Method:** End-to-end rehearsal and documentation audit.
- **Required Evidence (Planned):** Final bound project report, slide deck, and live demonstration execution logs.
- **Definition of Done:** Project report approved by supervisor.
