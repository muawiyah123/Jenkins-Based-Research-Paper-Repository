# Task 15 Report: Containerization (Docker) & Infrastructure Provisioning (Ansible)

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
| **Task Identifier** | Task 15 |
| **Milestone Phase** | Phase 4: Automation & Delivery |
| **Agile Sprint** | Weeks 10–11 |
| **User Story Mapping** | US-013 (Container Packaging), US-014 (Infrastructure Automation) |
| **Task Status** | **COMPLETED (100% Verified)** |

---

## 1. Task Objective & Requirements
Package the Spring Boot application into an immutable Docker container image using a multi-stage build, and provide an Ansible deployment playbook for automated host provisioning.

---

## 2. Technical Implementation Details
### Technical Implementation
1. **Multi-Stage Dockerfile (`Dockerfile`):**
   - Build Stage: `maven:3.9.9-eclipse-temurin-21` compiles and packages the JAR.
   - Runtime Stage: `eclipse-temurin:21-jre-jammy` provides a lightweight execution layer.
   - Security: Runs as non-root user `appuser` (UID 1001).
   - Port: Exposes port `8081`.
2. **Docker Compose (`docker-compose.yml`):**
   - Single-command orchestration with healthcheck (`/actuator/health` or `/`), port forwarding `8081:8081`, and restart policy `unless-stopped`.
3. **Ansible Deployment Playbook (`ansible/deploy.yml`):**
   - Tasks:
     1. Install Docker, Docker Compose, and prerequisites.
     2. Clone repository from GitHub remote.
     3. Build container image via `docker compose build`.
     4. Launch container via `docker compose up -d`.

---

## 3. Empirical Proof of Work & Verification
### Empirical Proof of Work
- **Terminal Execution Command:**
  ```powershell
  PS C:\Users\Muawiyah\Desktop\Jenkins-Based Research Paper Repository> docker build -t muawiyah123/research-paper-repository:latest .
  ```
- **Screenshot Evidence:**
  ![Docker Build CLI Verification](../evidence/docker-06-cli-build-verification.png)
  *Figure 15.1: PowerShell terminal verification of container build command.*

---

## 4. Acceptance Criteria Checklist
- [x] Multi-stage `Dockerfile` authored with non-root security execution
- [x] `docker-compose.yml` orchestrates service with container healthcheck
- [x] Ansible inventory and `deploy.yml` automate host provisioning
- [x] All configuration files committed and pushed to GitHub

---

## 5. Artifacts & Deliverables
- ``Dockerfile``
- ``.dockerignore``
- ``docker-compose.yml``
- ``ansible/inventory``
- ``ansible/deploy.yml``

---

*Task completed and verified by:*  
**Muawiyah Sahil Narvel** | Roll No: `23108A0020`  
Date: October 15, 2026
