# Task 08 Report: Paper Metadata Edit & Author Resubmission

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
| **Task Identifier** | Task 08 |
| **Milestone Phase** | Phase 2: Core Domain Model & Application MVP |
| **Agile Sprint** | Week 5 |
| **User Story Mapping** | US-005 (Paper Modification & Revision Resubmission) |
| **Task Status** | **COMPLETED (100% Verified)** |

---

## 1. Task Objective & Requirements
Allow authors to modify paper metadata when in `SUBMITTED` or `REVISION_REQUESTED` status while locking immutable terminal states.

---

## 2. Technical Implementation Details
### Technical Implementation
1. **Controller Endpoints (`PaperController.java`):**
   - `GET /papers/{id}/edit`: Renders edit form pre-populated with current paper data.
   - `POST /papers/{id}/edit`: Validates edits and updates repository record.
2. **Access Control & Ownership Guard:**
   - Verifies that `paper.getSubmitterUsername().equals(principal.getName())`. Foreign authors are denied access with an error alert.
3. **State Machine Transition Guard:**
   - Editing a paper in `REVISION_REQUESTED` state automatically transitions its status back to `SUBMITTED` (or `UNDER_REVIEW`) for peer re-evaluation.
   - Papers in terminal states (`ACCEPTED`, `REJECTED`) throw an `IllegalStateException` preventing any edits.

---

## 3. Empirical Proof of Work & Verification
### Empirical Proof of Work
- **Execution Command:**
  ```powershell
  PS C:\Users\Muawiyah\Desktop\Jenkins-Based Research Paper Repository> .\mvnw.cmd test -Dtest=PaperServiceTest
  ```
- **Observed Test Output:**
  ```text
  [INFO] Running com.muawiyah.repository.service.PaperServiceTest
  [INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.165 s
  [INFO] BUILD SUCCESS
  ```
- **Service Verification:** Tests verify ownership exceptions and state resets upon edit submission.

---

## 4. Acceptance Criteria Checklist
- [x] Only original author can access edit form
- [x] Editing papers in `ACCEPTED` or `REJECTED` status is strictly forbidden
- [x] Resubmitting a paper in `REVISION_REQUESTED` resets status for re-evaluation
- [x] All service unit tests pass cleanly

---

## 5. Artifacts & Deliverables
- ``src/main/resources/templates/papers/edit.html``
- ``src/main/java/com/muawiyah/repository/service/PaperService.java``
- ``src/test/java/com/muawiyah/repository/service/PaperServiceTest.java``

---

*Task completed and verified by:*  
**Muawiyah Sahil Narvel** | Roll No: `23108A0020`  
Date: October 15, 2026
