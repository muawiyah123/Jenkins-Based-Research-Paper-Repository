# Task 09 Report: In-Memory Spring Security 6 Role-Based Access Control

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
| **Task Identifier** | Task 09 |
| **Milestone Phase** | Phase 2: Core Domain Model & Application MVP |
| **Agile Sprint** | Week 6 |
| **User Story Mapping** | US-009 (Authentication & Role Authorization) |
| **Task Status** | **COMPLETED (100% Verified)** |

---

## 1. Task Objective & Requirements
Configure Spring Security 6 with in-memory development credentials, BCrypt password hashing, role-based URL protection, and CSRF defense.

---

## 2. Technical Implementation Details
### Technical Implementation
1. **Security Configuration (`SecurityConfig.java`):**
   - Password Encoder: `BCryptPasswordEncoder`.
   - In-Memory User Details Manager:
     - `researcher` (Password: `password`, Role: `RESEARCHER`)
     - `reviewer` (Password: `password`, Role: `REVIEWER`)
     - `admin` (Password: `password`, Roles: `ADMIN`, `REVIEWER`, `RESEARCHER`)
2. **Route Authorization Policy:**
   - Public: `/`, `/papers`, `/papers/{id}`, `/login`, `/css/**`, `/js/**`
   - Researcher: `/papers/submit`, `/papers/*/edit`
   - Reviewer: `/papers/review`, `/papers/*/review`, `/papers/*/status`
   - Admin: `/dashboard`, `/admin/**`
3. **Security Defenses:**
   - CSRF protection enabled on all POST endpoints.
   - Custom login page mapped at `/login` with Academic Development Accounts helper card.

---

## 3. Empirical Proof of Work & Verification
### Empirical Proof of Work
- **Execution Command:**
  ```powershell
  PS C:\Users\Muawiyah\Desktop\Jenkins-Based Research Paper Repository> .\mvnw.cmd test -Dtest=SecurityConfigTest
  ```
- **Observed Test Output:**
  ```text
  [INFO] Running com.muawiyah.repository.config.SecurityConfigTest
  [INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.358 s
  [INFO] BUILD SUCCESS
  ```
- **Screenshot Evidence:**
  ![Spring Security Login Portal](../evidence/ui-03-login.png)
  *Figure 9.1: Spring Security 6 Sign-In Portal with Academic Development Accounts.*

---

## 4. Acceptance Criteria Checklist
- [x] BCrypt password hashing implemented for all in-memory accounts
- [x] Role authorization enforced across researcher, reviewer, and admin routes
- [x] CSRF protection enabled on all mutating state requests
- [x] 12/12 security slice tests pass without errors

---

## 5. Artifacts & Deliverables
- ``src/main/java/com/muawiyah/repository/config/SecurityConfig.java``
- ``src/main/resources/templates/login.html``
- ``src/test/java/com/muawiyah/repository/config/SecurityConfigTest.java``

---

*Task completed and verified by:*  
**Muawiyah Sahil Narvel** | Roll No: `23108A0020`  
Date: October 15, 2026
