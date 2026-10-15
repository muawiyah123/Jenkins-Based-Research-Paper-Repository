# Task 11 Report: Administrative Metrics Dashboard

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
| **Task Identifier** | Task 11 |
| **Milestone Phase** | Phase 2: Core Domain Model & Application MVP |
| **Agile Sprint** | Week 7 |
| **User Story Mapping** | US-008 (Administrative Repository Analytics) |
| **Task Status** | **COMPLETED (100% Verified)** |

---

## 1. Task Objective & Requirements
Develop an administrative metrics dashboard displaying aggregate paper counts, lifecycle status distributions, acceptance rate calculations, and recent submission logs.

---

## 2. Technical Implementation Details
### Technical Implementation
1. **Controller Layer (`DashboardController.java`):**
   - Endpoint: `GET /dashboard`.
   - Computes:
     - Total papers submitted
     - Count per state (`SUBMITTED`, `UNDER_REVIEW`, `ACCEPTED`, `REJECTED`, `REVISION_REQUESTED`)
     - Acceptance rate percentage
     - Recent 5 submissions with timestamp and author attribution
2. **User Interface (`dashboard.html`):**
   - Responsive KPI card layout with distinct accent colors:
     - Blue: Total Papers
     - Sky Blue: Submitted
     - Amber: Under Review
     - Purple: Revision Requested
     - Green: Accepted
     - Red: Rejected
   - Direct action buttons to Explore Catalog and Review Workspace.

---

## 3. Empirical Proof of Work & Verification
### Empirical Proof of Work
- **Execution Command:**
  ```powershell
  PS C:\Users\Muawiyah\Desktop\Jenkins-Based Research Paper Repository> .\mvnw.cmd test -Dtest=DashboardControllerTest
  ```
- **Observed Test Output:**
  ```text
  [INFO] Running com.muawiyah.repository.controller.DashboardControllerTest
  [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.143 s
  [INFO] BUILD SUCCESS
  ```
- **Screenshot Evidence:**
  ![Admin Dashboard](../evidence/ui-06-admin-dashboard.png)
  *Figure 11.1: Administrative Metrics Dashboard displaying live KPI cards.*

---

## 4. Acceptance Criteria Checklist
- [x] Dashboard accessible at `/dashboard` to authenticated users
- [x] Displays accurate aggregate counts across all 5 review statuses
- [x] Recent submissions stream rendered with correct metadata
- [x] 5/5 MockMvc slice tests pass cleanly

---

## 5. Artifacts & Deliverables
- ``src/main/java/com/muawiyah/repository/controller/DashboardController.java``
- ``src/main/resources/templates/dashboard.html``
- ``src/test/java/com/muawiyah/repository/controller/DashboardControllerTest.java``

---

*Task completed and verified by:*  
**Muawiyah Sahil Narvel** | Roll No: `23108A0020`  
Date: October 15, 2026
