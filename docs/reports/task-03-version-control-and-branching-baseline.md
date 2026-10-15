# Task 03 Report: Version Control Baseline & Dual-Branch Git Init

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
| **Task Identifier** | Task 03 |
| **Milestone Phase** | Phase 1: Foundation & Planning |
| **Agile Sprint** | Week 3 |
| **User Story Mapping** | US-001 (Source Control Architecture) |
| **Task Status** | **COMPLETED (100% Verified)** |

---

## 1. Task Objective & Requirements
Initialize the Git version control repository, establish `.gitignore` hygiene, and implement a dual-branch branching model (`main` and `development`).

---

## 2. Technical Implementation Details
### Technical Implementation
1. **Git Ignore Configuration (`.gitignore`):**
   - Configured exclusions for compiled artifacts (`target/`, `*.jar`, `*.war`), IDE workspaces (`.idea/`, `*.iml`, `.vscode/`, `.settings/`), temporary test profiles, and OS metadata (`Thumbs.db`, `Desktop.ini`).
2. **Branching Model:**
   - `main`: Production release baseline representing faculty-reviewed milestone deliveries.
   - `development`: Active integration branch where feature branches are merged.
   - Feature branches follow conventional naming: `feature/<task-name>`.
3. **GitHub Remote Synchronization:**
   - Linked repository to `https://github.com/muawiyah123/Jenkins-Based-Research-Paper-Repository.git`.

---

## 3. Empirical Proof of Work & Verification
### Empirical Proof of Work
- **Execution Command:**
  ```powershell
  PS C:\Users\Muawiyah\Desktop\Jenkins-Based Research Paper Repository> git log --graph --oneline -n 10
  ```
- **Observed Commit Graph:**
  ```text
  * 7dfd96a Merge branch 'feature/containerization-and-ansible' into development
  |\  
  | * a0384cb feat: containerize Spring Boot app with multi-stage Dockerfile and Ansible
  |/  
  * cdf55c4 feat: implement headless Chrome Selenium E2E smoke tests
  * 318c4e1 feat: implement Jenkins declarative CI pipeline with multi-stage build
  * f4be709 feat: implement custom error templates and comprehensive test suite
  ```
- **Screenshot Evidence:**
  ![Git Log and Branch Graph](../evidence/cmd-03-git-log.png)
  *Figure 3.1: Clean Git commit history and branch graph.*

---

## 4. Acceptance Criteria Checklist
- [x] Git repository initialized with complete `.gitignore` rules
- [x] `main` branch locked to verified, tested release tags
- [x] `development` branch synchronized with remote GitHub origin
- [x] Conventional commit prefixes (`feat:`, `docs:`, `test:`, `fix:`) applied

---

## 5. Artifacts & Deliverables
- ``.gitignore``
- `Git repository with `main` and `development` branches`
- `Remote synchronization on GitHub`

---

*Task completed and verified by:*  
**Muawiyah Sahil Narvel** | Roll No: `23108A0020`  
Date: October 15, 2026
