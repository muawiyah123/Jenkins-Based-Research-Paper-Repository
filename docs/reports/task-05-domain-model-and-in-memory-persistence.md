# Task 05 Report: Domain Model & In-Memory Persistence

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
| **Task Identifier** | Task 05 |
| **Milestone Phase** | Phase 2: Core Domain Model & Application MVP |
| **Agile Sprint** | Week 4 |
| **User Story Mapping** | US-002 (Domain Architecture & Data Schema) |
| **Task Status** | **COMPLETED (100% Verified)** |

---

## 1. Task Objective & Requirements
Construct the core JPA entity `Paper` with 12 distinct academic attributes, audit timestamps, the `ReviewStatus` lifecycle enumeration, and custom Spring Data JPA query repositories.

---

## 2. Technical Implementation Details
### Technical Implementation
1. **Domain Entity (`Paper.java`):**
   - Attributes:
     - `Long id` (Identity primary key)
     - `String title` (255 chars, indexed)
     - `String authors` (Comma-separated authors)
     - `String abstractText` (CLOB/TEXT up to 5000 chars)
     - `String keywords` (Search indexable tags)
     - `Integer publicationYear` (Validation: 1900 to 2100)
     - `String doiUrl` (DOI or HTTP URL pattern)
     - `String submitterEmail` (Private, confidential author email)
     - `String submitterUsername` (Server-side bound author identifier)
     - `ReviewStatus reviewStatus` (Enumerated state)
     - `LocalDateTime createdAt` / `LocalDateTime updatedAt` (Audit timestamps)
2. **Review Status Lifecycle Enum (`ReviewStatus.java`):**
   - States: `SUBMITTED`, `UNDER_REVIEW`, `ACCEPTED`, `REJECTED`, `REVISION_REQUESTED`.
   - Defines `isTerminal()` helper: `ACCEPTED` and `REJECTED` are immutable.
3. **JPA Repository (`PaperRepository.java`):**
   - Methods:
     - `findByReviewStatus(ReviewStatus status, Pageable pageable)`
     - `searchPapers(String keyword, Pageable pageable)` (Case-insensitive multi-column query)
     - `countByReviewStatus(ReviewStatus status)`

---

## 3. Empirical Proof of Work & Verification
### Empirical Proof of Work
- **Execution Command:**
  ```powershell
  PS C:\Users\Muawiyah\Desktop\Jenkins-Based Research Paper Repository> .\mvnw.cmd test -Dtest=PaperRepositoryTest
  ```
- **Observed Test Output:**
  ```text
  [INFO] Running com.muawiyah.repository.repository.PaperRepositoryTest
  Hibernate: insert into papers (...) values (...)
  Hibernate: select p1_0.id, p1_0.title from papers p1_0 where lower(p1_0.title) like ? ...
  [INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.266 s
  [INFO] BUILD SUCCESS
  ```
- **Screenshot Evidence:**
  ![JPA Repository Execution](../evidence/cmd-01-mvnw-test.png)
  *Figure 5.1: 5/5 JPA repository tests passing in standard test suite.*

---

## 4. Acceptance Criteria Checklist
- [x] `Paper.java` entity model mapped with all 12 academic attributes
- [x] `ReviewStatus.java` enum enforces 5 lifecycle states with terminal flags
- [x] JPA custom query performs case-insensitive search across title, author, and keywords
- [x] All 5 slice tests in `PaperRepositoryTest.java` pass cleanly

---

## 5. Artifacts & Deliverables
- ``src/main/java/com/muawiyah/repository/model/Paper.java``
- ``src/main/java/com/muawiyah/repository/model/ReviewStatus.java``
- ``src/main/java/com/muawiyah/repository/repository/PaperRepository.java``
- ``src/test/java/com/muawiyah/repository/repository/PaperRepositoryTest.java``

---

*Task completed and verified by:*  
**Muawiyah Sahil Narvel** | Roll No: `23108A0020`  
Date: October 15, 2026
