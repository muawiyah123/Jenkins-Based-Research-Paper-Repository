# Task 04 Report: Public Home & Navigation Scaffold

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
| **Task Identifier** | Task 04 |
| **Milestone Phase** | Phase 1: Foundation & Planning |
| **Agile Sprint** | Week 3 |
| **User Story Mapping** | US-001 (Public Portal Landing) |
| **Task Status** | **COMPLETED (100% Verified)** |

---

## 1. Task Objective & Requirements
Design and build a responsive public home portal with glassmorphism hero styling, platform foundation metrics, accessible navigation, and candidate attribution banner.

---

## 2. Technical Implementation Details
### Technical Implementation
1. **Controller Layer (`HomeController.java`):**
   - Mapped `GET /` to render `index.html`.
   - Injected system statistics and active profile metadata into the view model.
2. **Thymeleaf Template (`src/main/resources/templates/index.html`):**
   - Responsive Bootstrap 5 layout with custom typography (`Inter` via Google Fonts).
   - Candidate Attribution Badge: `Muawiyah Narvel (23108A0020)`.
   - System Foundation Baseline Card: Java 21 LTS, Spring Boot 3.4.3, H2 Database, Apache Maven, JUnit 5.
   - Direct CTA links to Browse Catalog (`/papers`) and H2 Database Console (`/h2-console`).
3. **Automated Unit Tests (`HomeControllerTest.java`):**
   - Validated HTTP 200 OK, view name `"index"`, and candidate badge presence in DOM.

---

## 3. Empirical Proof of Work & Verification
### Empirical Proof of Work
- **Execution Command:**
  ```powershell
  PS C:\Users\Muawiyah\Desktop\Jenkins-Based Research Paper Repository> .\mvnw.cmd test -Dtest=HomeControllerTest
  ```
- **Observed Test Output:**
  ```text
  [INFO] Running com.muawiyah.repository.controller.HomeControllerTest
  [INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.089 s
  [INFO] BUILD SUCCESS
  ```
- **Screenshot Evidence:**
  ![Public Home Portal](../evidence/ui-01-homepage.png)
  *Figure 4.1: Rendered Public Home Portal displaying candidate attribution banner.*

---

## 4. Acceptance Criteria Checklist
- [x] Public home page accessible at `GET /` without authentication
- [x] Hero banner displays platform specifications and candidate credentials
- [x] Navigation header links to Catalog, Submit, Review Portal, and Dashboard
- [x] MockMvc tests confirm view resolution and HTTP 200 response

---

## 5. Artifacts & Deliverables
- ``src/main/java/com/muawiyah/repository/controller/HomeController.java``
- ``src/main/resources/templates/index.html``
- ``src/test/java/com/muawiyah/repository/controller/HomeControllerTest.java``

---

*Task completed and verified by:*  
**Muawiyah Sahil Narvel** | Roll No: `23108A0020`  
Date: October 15, 2026
