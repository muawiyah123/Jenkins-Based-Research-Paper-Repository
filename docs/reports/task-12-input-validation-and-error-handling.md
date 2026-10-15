# Task 12 Report: Input Validation, Error Handling & Comprehensive Test Suite

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
| **Task Identifier** | Task 12 |
| **Milestone Phase** | Phase 2: Core Domain Model & Application MVP |
| **Agile Sprint** | Week 7 |
| **User Story Mapping** | US-010 (Quality Assurance & Validation) |
| **Task Status** | **COMPLETED (100% Verified)** |

---

## 1. Task Objective & Requirements
Implement custom user-friendly error templates (403, 404, 500) and assemble a comprehensive 76-test automated test suite across all architectural layers.

---

## 2. Technical Implementation Details
### Technical Implementation
1. **Custom Error Templates (`src/main/resources/templates/error/`):**
   - `403.html`: Access Denied with role permission guidance and return home CTA.
   - `404.html`: Resource Not Found with catalog exploration CTA.
   - `500.html`: Internal Server Error with clean incident notice.
2. **Comprehensive Automated Test Suite:**
   - 76 standard tests spanning 8 test classes:
     - `RepositoryApplicationTests`: 1 test (Context load)
     - `PaperRepositoryTest`: 5 tests (JPA queries)
     - `PaperServiceTest`: 25 tests (Business logic & state machine)
     - `HomeControllerTest`: 3 tests (Web slice)
     - `PaperControllerTest`: 15 tests (Submission, catalog, edit slice)
     - `ReviewControllerTest`: 10 tests (Review slice)
     - `DashboardControllerTest`: 5 tests (Metrics slice)
     - `SecurityConfigTest`: 12 tests (RBAC security slice)

---

## 3. Empirical Proof of Work & Verification
### Empirical Proof of Work
- **Execution Command:**
  ```powershell
  PS C:\Users\Muawiyah\Desktop\Jenkins-Based Research Paper Repository> .\mvnw.cmd test
  ```
- **Observed Test Results:**
  ```text
  [INFO] Results:
  [INFO] Tests run: 76, Failures: 0, Errors: 0, Skipped: 0
  [INFO] ------------------------------------------------------------------------
  [INFO] BUILD SUCCESS
  [INFO] Total time:  20.736 s
  ```
- **Screenshot Evidence:**
  ![Standard Test Suite Execution](../evidence/cmd-01-mvnw-test.png)
  *Figure 12.1: PowerShell execution of 76 standard tests passing 100% green.*

---

## 4. Acceptance Criteria Checklist
- [x] Custom error pages (403, 404, 500) rendered cleanly
- [x] 76 automated tests execute with zero failures
- [x] Test execution time under 25 seconds for rapid CI feedback
- [x] 100% passing test assertion across all architectural slices

---

## 5. Artifacts & Deliverables
- ``src/main/resources/templates/error/403.html``
- ``src/main/resources/templates/error/404.html``
- ``src/main/resources/templates/error/500.html``
- `8 test classes totaling 76 tests`

---

*Task completed and verified by:*  
**Muawiyah Sahil Narvel** | Roll No: `23108A0020`  
Date: October 15, 2026
