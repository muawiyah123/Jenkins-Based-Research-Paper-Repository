# Task 13 Report: Jenkins CI Declarative Pipeline

**Candidate Information:**
- **Student Name:** Muawiyah Sahil Narvel
- **Roll Number:** 23108A0020
- **Course / Lab:** DevOps & Cloud Automation / Software Engineering Laboratory
- **GitHub Repository:** [muawiyah123/Jenkins-Based-Research-Paper-Repository](https://github.com/muawiyah123/Jenkins-Based-Research-Paper-Repository.git)
- **Active Branches:** `main` (Production Release), `development` (Active Sprint)

---


## Task Metadata
| Attribute | Specification |
|---|---|
| **Task Identifier** | Task 13 |
| **Milestone Phase** | Phase 3: DevOps CI/CD |
| **Agile Sprint** | Week 8 |
| **User Story Mapping** | US-011 (Continuous Integration Pipeline Automation) |
| **Task Status** | **COMPLETED (100% Verified)** |

---

## 1. Task Objective & Requirements
Author a declarative multi-stage Jenkinsfile to automate checkout, toolchain verification, compilation, unit testing, packaging, and JUnit test result archiving.

---

## 2. Technical Implementation Details
### Technical Implementation
1. **Declarative Pipeline (`Jenkinsfile`):**
   - Stage 1: `Checkout` &mdash; Pulls code from Git SCM targeting branch `*/main`.
   - Stage 2: `Verify Toolchain` &mdash; Displays Java and Maven versions.
   - Stage 3: `Compile` &mdash; Executes `mvn clean compile`.
   - Stage 4: `Test` &mdash; Runs `mvn test` verifying unit and integration test suites.
   - Stage 5: `Package` &mdash; Compiles executable JAR `mvn package -DskipTests`.
   - Stage 6: `Post-Build Actions` &mdash; Archives `target/*.jar` and publishes JUnit XML test reports (`target/surefire-reports/*.xml`).
2. **Jenkins Controller Configuration:**
   - Connected to local controller on port 8080.
   - Created pipeline project `research-paper-repository`.

---

## 3. Empirical Proof of Work & Verification
### Empirical Proof of Work
- **Jenkins Pipeline Build Details:**
  - Build #1 (14 Oct 2026, 14:05:53): SUCCESS, User: `Muawiyah Sahil Narvel`, Commit: `2e7188d`, Artifact: `repository-0.0.1-SNAPSHOT.jar` (53.44 MiB), 0 failures.
  - Multi-Build Trend: Builds #1, #2, #3 all passed with test count scaling from 28 to 36 to 39 passing tests (100% green).
- **Screenshot Evidence:**
  ![Jenkins Welcome](../evidence/jenkins-01-dashboard-welcome.png)
  *Figure 13.1: Jenkins controller running on port 8080.*
  
  ![Jenkins Pipeline Configuration](../evidence/jenkins-02-pipeline-configuration.png)
  *Figure 13.2: Pipeline SCM configuration targeting `*/main` and `Jenkinsfile`.*
  
  ![Jenkins Build #1 Success](../evidence/jenkins-04-build1-success-details.png)
  *Figure 13.3: Jenkins Build #1 execution success view.*
  
  ![Jenkins Test Trend Graph](../evidence/jenkins-05-build-trend-test-results.png)
  *Figure 13.4: Jenkins Test Result Trend graph showing 3 consecutive passing builds.*

---

## 4. Acceptance Criteria Checklist
- [x] Declarative `Jenkinsfile` authored with 6 distinct pipeline stages
- [x] Automated compilation, test execution, and JAR packaging
- [x] JUnit XML test reports published with interactive trend graphs
- [x] Verified live on Jenkins controller with multiple green builds

---

## 5. Artifacts & Deliverables
- ``Jenkinsfile``
- `Jenkins job `research-paper-repository` on port 8080`
- `Archived JAR artifact `repository-0.0.1-SNAPSHOT.jar``

---

*Task completed and verified by:*  
**Muawiyah Sahil Narvel** | Roll No: `23108A0020`  
Date: October 15, 2026
