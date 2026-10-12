# Problem Statement: Jenkins-Based Research Paper Repository

**Student Name:** Muawiyah Narvel  
**Roll Number:** 23108A0020  
**Project Title:** Jenkins-Based Research Paper Repository  
**Academic Term:** Weeks 1–3 Foundation Milestone  
**Document Reference:** `docs/week-01/problem-statement.md`  
**Status:** Approved  

---

## 1. Project Background
In higher education and scientific research environments, academic institutions produce and review a steady volume of research papers, conference manuscripts, and technical reports. Managing this scholarship requires tracking detailed metadata—such as titles, author attributions, abstracts, keywords, publication years, digital object identifiers (DOIs), and peer-review progression stages.

Concurrently, modern software engineering requires academic software projects to move beyond ad-hoc manual execution on personal laptops. Real-world systems require automated quality verification, predictable compilation, and reproducible deployments through Continuous Integration and Continuous Delivery (CI/CD). This project addresses both the academic domain challenge of managing research metadata and the systems engineering challenge of automating the software delivery lifecycle.

---

## 2. Existing Problem and Limitations

### 2.1 Domain & Information Management Limitations
1. **Unstructured and Fragmented Metadata:** Academic departments frequently rely on decentralized spreadsheets, local file folders, or email attachments to keep track of submissions. This leads to version mismatch, lost abstracts, and missing publication metadata.
2. **Opaque Review Lifecycle:** Tracking whether a paper is submitted, under peer review, accepted, rejected, or requiring revision is difficult without a centralized tracking mechanism.
3. **Inefficient Search and Retrieval:** Without indexed keyword, author, or title querying, discovering existing institutional research or avoiding duplicated topics requires tedious manual catalog browsing.
4. **Lack of Role-Based Boundaries:** Spreadsheets lack automated role segmentation, meaning any viewer can accidentally modify paper records, change reviewer determinations, or delete submissions.

### 2.2 Engineering & Delivery Limitations
1. **Manual and Fragile Builds:** Academic projects are frequently compiled and launched manually via local IDEs without standardized build validation.
2. **"Works on My Machine" Syndrome:** Differences in local Java runtimes, environment variables, and OS configurations cause applications to fail when executed by supervisors or deployed to evaluation servers.
3. **Absence of Continuous Quality Checks:** Code changes are rarely validated against automated regression tests before integration, risking broken functionality going undetected until presentation time.
4. **Labor-Intensive Deployment:** Deploying updates or configuring web servers manually is error-prone, unversioned, and difficult to audit or repeat reliably.

---

## 3. Proposed Solution
The proposed solution is the **Jenkins-Based Research Paper Repository**, a dual-faceted initiative:

1. **A Spring Boot Web Application:** A clean, server-side rendered Java 21 web application utilizing Spring MVC, Thymeleaf, Bootstrap, Spring Data JPA, and an initial embedded H2 relational database. It provides an intuitive interface for researchers, reviewers, and administrators to submit, inspect, search, and update paper metadata and review statuses.
2. **An Automated DevOps Delivery Pipeline:** A CI/CD pipeline driven by Jenkins that automates source retrieval, Maven compilation, automated JUnit unit and integration testing, Selenium end-to-end browser testing, Docker container packaging, and Ansible-based deployment configuration.

---

## 4. Main Objectives

### 4.1 Academic & Application Objectives
- Build a centralized repository to record and maintain research paper metadata (title, authors, abstract, keywords, publication year, DOI, submitter, review status).
- Implement a search and filtering mechanism across title, author, and keyword fields.
- Provide a clear state machine for review workflow transitions (`SUBMITTED` -> `UNDER_REVIEW` -> `ACCEPTED` / `REJECTED` / `REVISION_REQUESTED`).
- Provide an informative dashboard summarizing institutional research metrics.
- Enforce role-based operational permissions across Researchers, Reviewers, and Administrators.

### 4.2 Engineering & DevOps Objectives
- Standardize build automation using Apache Maven and the Maven Wrapper (`mvnw`).
- Ensure consistent compatibility with Java 21 LTS across all stages.
- Automate build verification and regression testing on every code commit via a Jenkins multibranch or declarative pipeline.
- Implement automated functional verification using JUnit 5 and Selenium WebDriver.
- Containerize the application using Docker to ensure environment parity between development and deployment.
- Automate target server provisioning and configuration using Ansible playbooks.

---

## 5. Intended Users and Roles

| Role | Responsibilities & Typical Actions |
| :--- | :--- |
| **Researcher** | Submits paper metadata; views public repository; searches papers; edits metadata for own submissions while still draft/submitted. |
| **Reviewer** | Inspects assigned paper details and abstracts; updates review statuses and evaluation notes. |
| **Administrator** | Manages repository configurations; oversees user role permissions; monitors repository health and system metrics. |

---

## 6. Functional Goals
- **FG-01 (Paper Record Creation):** Form interface to input title, authors, abstract, keywords, publication year, and optional DOI/URL.
- **FG-02 (Catalog Browsing & Detail Inspection):** Paginated or tabulated list of papers with a dedicated single-paper detail view.
- **FG-03 (Metadata Modification):** Form to update title, keywords, abstract, and details with validation guardrails.
- **FG-04 (Multi-attribute Search):** Query engine supporting filtering by title, author substring, or keyword match.
- **FG-05 (Review Status Workflow):** Managed transitions across formal review status enumerations.
- **FG-06 (Summary Dashboard):** High-level view showing total paper count, acceptance rates, and status distributions.
- **FG-07 (Role-Based Authorization):** Protecting state transitions and administrative actions from unauthorized access.
- **FG-08 (Relational Persistence):** Safe database storage with transactional integrity.

---

## 7. Non-Functional Goals
- **NF-01 (Build Reproducibility):** Zero reliance on local IDE-specific configuration files; buildable via a single command (`./mvnw clean verify`).
- **NF-02 (Maintainability & Clean Architecture):** Strict separation of concerns (Controller, Service, Repository, Entity, DTO).
- **NF-03 (Reliability & Testability):** Unit and integration test coverage for all service rules and persistence queries.
- **NF-04 (Performance & Lightweight Footprint):** Fast startup and sub-second page rendering using lightweight Spring MVC and Thymeleaf.
- **NF-05 (Usability & Accessibility):** Responsive HTML5 layout powered by Bootstrap, usable on standard desktop and mobile browsers.

---

## 8. Project Scope

### 8.1 In-Scope (Planned Across Full Semester)
- Paper metadata creation, retrieval, catalog browsing, metadata editing, and review status management (record deletion is explicitly excluded from the MVP scope).
- Search and status workflow management.
- User authentication and authorization (Spring Security in later milestone).
- Embedded H2 database for local development, with clean migration path to PostgreSQL if required.
- Jenkins CI pipeline for automated testing and packaging.
- Containerization with Docker.
- Deployment configuration automation via Ansible.
- End-to-end automated UI smoke testing via Selenium WebDriver.

### 8.2 Explicitly Out-of-Scope (To Prevent Scope Creep)
- Automated plagiarism detection algorithms or integration with commercial turnitin-like APIs.
- AI-based paper summarization or generative text tools.
- Direct binary PDF/Word parsing or full-text OCR indexing (focus is metadata management).
- Cloud-native microservices, Kubernetes clusters, or multi-cloud orchestrations.
- Complex multi-stage payment or journal publishing fee gateways.

---

## 9. Assumptions and Limitations
1. **Academic Focus:** Designed primarily as an institutional prototype demonstrating robust software delivery practices rather than a commercial publisher platform.
2. **Initial Storage:** Initial development utilizes an in-memory or file-backed H2 database to facilitate zero-dependency evaluation before introducing external database containers.
3. **Deployment Strategy Flag:** The project specification references Tomcat/Nginx deployment. This will be clarified with college faculty to confirm whether Spring Boot’s production-standard embedded Tomcat running behind an Nginx reverse proxy satisfies the college rubric.
4. **Ansible Target-Host Environment Flag:** The target server environment (local virtual machine, secondary physical machine, or container host) for executing Ansible playbooks is an external infrastructure dependency subject to faculty clarification and resource availability.

---

## 10. Expected Outcomes and Deliverables
1. **Working Application:** A fully operational Spring Boot web application adhering to Java 21 standards.
2. **Test Suites:** Comprehensive JUnit 5 and Selenium test suites verifying domain rules and UI interactions.
3. **DevOps Artifacts:** `Jenkinsfile` pipeline script, `Dockerfile`, and Ansible configuration playbooks.
4. **Structured Documentation:** Complete SRS, architecture diagrams, testing strategies, and weekly verified execution logs.

---

## 11. The Role of DevOps and CI/CD
In traditional college projects, deployment is treated as a manual, one-time afterthought. In this project, DevOps is treated as a first-class engineering discipline:
- **Continuous Integration (CI):** Every push to Git triggers Jenkins to run `mvnw clean test`, ensuring broken code is caught immediately.
- **Continuous Packaging:** Verified builds are packaged into immutable Docker container images.
- **Continuous Delivery & Configuration:** Ansible automates environment provisioning, eliminating human error in server configuration.
- **Feedback Loop:** Test results and build statuses provide immediate, auditable feedback on software health.
