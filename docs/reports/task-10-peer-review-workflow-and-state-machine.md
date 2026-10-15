# Task 10 Report: Peer-Review Workflow & 5-State Lifecycle State Machine

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
| **Task Identifier** | Task 10 |
| **Milestone Phase** | Phase 2: Core Domain Model & Application MVP |
| **Agile Sprint** | Week 6 |
| **User Story Mapping** | US-006 (Reviewer Queue), US-007 (Status Decision Execution) |
| **Task Status** | **COMPLETED (100% Verified)** |

---

## 1. Task Objective & Requirements
Build the peer-review queue and implement a validated, immutable 5-state lifecycle state machine for evaluating academic submissions.

---

## 2. Technical Implementation Details
### Technical Implementation
1. **Review Portal Controller (`PaperController.java`):**
   - `GET /papers/review`: Reviewer portal displaying submission queue with status filter tabs.
   - `GET /papers/{id}/review`: Decision execution form showing permitted next transitions.
   - `POST /papers/{id}/status`: Executes status update with reviewer feedback comments.
2. **State Machine Transition Rules:**
   - `SUBMITTED` &rarr; `UNDER_REVIEW`
   - `UNDER_REVIEW` &rarr; `ACCEPTED` (Terminal)
   - `UNDER_REVIEW` &rarr; `REJECTED` (Terminal)
   - `UNDER_REVIEW` &rarr; `REVISION_REQUESTED`
   - `REVISION_REQUESTED` &rarr; `SUBMITTED` (Upon author resubmission)
3. **Terminal State Immutability:**
   - Attempting illegal transitions (e.g. `ACCEPTED` &rarr; `SUBMITTED`) throws `IllegalStateException`.

---

## 3. Empirical Proof of Work & Verification
### Empirical Proof of Work
- **Screenshot Evidence:**
  ![Peer Review Queue](../evidence/ui-05-review-queue.png)
  *Figure 10.1: Peer-Review Lifecycle Management Queue under `ROLE_REVIEWER`.*
- **Unit Verification:** 10 state machine tests in `PaperServiceTest.java` passed verifying all valid and invalid transitions.

---

## 4. Acceptance Criteria Checklist
- [x] Review portal displays submissions filterable by review status
- [x] Reviewers can transition papers between permitted lifecycle states
- [x] Terminal states (`ACCEPTED`, `REJECTED`) cannot be transitioned
- [x] All state machine unit tests pass

---

## 5. Artifacts & Deliverables
- ``src/main/resources/templates/papers/review-portal.html``
- ``src/main/resources/templates/papers/review.html``
- ``src/main/java/com/muawiyah/repository/service/PaperService.java``

---

*Task completed and verified by:*  
**Muawiyah Sahil Narvel** | Roll No: `23108A0020`  
Date: October 15, 2026
