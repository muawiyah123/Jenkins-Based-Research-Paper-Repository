# Task 07 Report: Public Catalog & Real-Time Keyword Search

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
| **Task Identifier** | Task 07 |
| **Milestone Phase** | Phase 2: Core Domain Model & Application MVP |
| **Agile Sprint** | Week 5 |
| **User Story Mapping** | US-003 (Public Catalog), US-004 (Keyword Discovery) |
| **Task Status** | **COMPLETED (100% Verified)** |

---

## 1. Task Objective & Requirements
Construct the public paper catalog with real-time multi-field keyword search, pagination, and strict author email privacy protection.

---

## 2. Technical Implementation Details
### Technical Implementation
1. **Catalog Exploration Endpoints:**
   - `GET /papers`: Displays paginated list of papers with search filter.
   - `GET /papers/{id}`: Displays full academic metadata card for individual paper.
2. **Search Logic (`PaperService.java`):**
   - Performs case-insensitive matching across `title`, `authors`, and `keywords`.
3. **Strict Privacy Enforcement Rule:**
   - **Privacy Policy:** The author's confidential email (`submitterEmail`) is **strictly omitted** from public catalog listings and public detail cards.
   - Verified via both slice tests and headless Selenium DOM queries.

---

## 3. Empirical Proof of Work & Verification
### Empirical Proof of Work
- **Screenshot Evidence:**
  ![Public Catalog and Privacy](../evidence/ui-02-catalog.png)
  *Figure 7.1: Public Research Paper Catalog with search bar and redacted submitter emails.*
- **DOM Privacy Verification:**
  Selenium E2E test `testPublicCatalogDoesNotExposeSubmitterEmail` checks the rendered DOM and asserts that no `@` or email field exists in the public catalog.

---

## 4. Acceptance Criteria Checklist
- [x] Catalog accessible to anonymous and authenticated users
- [x] Keyword search filters dynamically across title, author, and keywords
- [x] Author email addresses are completely excluded from public DOM rendering
- [x] Unit and integration tests pass verifying privacy rules

---

## 5. Artifacts & Deliverables
- ``src/main/resources/templates/papers/catalog.html``
- ``src/main/resources/templates/papers/detail.html``
- ``src/main/java/com/muawiyah/repository/service/PaperService.java``

---

*Task completed and verified by:*  
**Muawiyah Sahil Narvel** | Roll No: `23108A0020`  
Date: October 15, 2026
