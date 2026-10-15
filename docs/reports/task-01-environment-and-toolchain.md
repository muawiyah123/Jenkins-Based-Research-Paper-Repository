# Task 01 Report: Environment & Toolchain Verification

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
| **Task Identifier** | Task 01 |
| **Milestone Phase** | Phase 1: Foundation & Planning |
| **Agile Sprint** | Week 3 |
| **User Story Mapping** | US-001 (Developer Toolchain Initialization) |
| **Task Status** | **COMPLETED (100% Verified)** |

---

## 1. Task Objective & Requirements
Verify that Java 21 LTS, Apache Maven 3.9+, Git version control, and IDE dependencies are correctly installed, configured, and free of architectural or environment conflicts.

---

## 2. Technical Implementation Details
### Technical Implementation
1. **JDK 21 LTS Verification:**
   - Validated Eclipse Temurin JDK `21.0.12.1+7` installation.
   - Configured `pom.xml` with `<java.version>21</java.version>` and compiler plugin `<release>21</release>`.
2. **Maven Wrapper Configuration:**
   - Configured Apache Maven Wrapper (`mvnw.cmd` and `mvnw`) with Maven 3.9.9.
   - Verified that all builds can execute portably without requiring global Maven installation on host or CI worker machines.
3. **Port Allocation Strategy:**
   - Set Spring Boot embedded Tomcat server port to `8081` in `application.properties`.
   - Reserved port `8080` specifically for the Jenkins CI automation controller to prevent port conflicts.

---

## 3. Empirical Proof of Work & Verification
### Empirical Proof of Work
- **Execution Command:**
  ```powershell
  PS C:\Users\Muawiyah\Desktop\Jenkins-Based Research Paper Repository> .\mvnw.cmd -v
  ```
- **Observed Toolchain Output:**
  ```text
  Apache Maven 3.9.9 (8e8579a9e76f7d015ee5ec7bfcdc97d260186937)
  Maven home: C:\Users\Muawiyah\.m2\wrapper\dists\apache-maven-3.9.9-bin\...
  Java version: 21.0.12.1, vendor: Eclipse Adoptium, runtime: C:\Program Files\Eclipse Adoptium\jdk-21.0.12.1-hotspot
  Default locale: en_IN, platform encoding: UTF-8
  OS name: "windows 11", version: "10.0", arch: "amd64", family: "windows"
  ```
- **Screenshot Evidence:**
  ![Toolchain Verification](../evidence/cmd-01-mvnw-test.png)
  *Figure 1.1: PowerShell Toolchain & Build Verification.*

---

## 4. Acceptance Criteria Checklist
- [x] Java 21 LTS Temurin verified and set as default compilation target
- [x] Maven wrapper (`mvnw.cmd`) operational with zero external dependencies
- [x] Port 8081 allocated for application, reserving port 8080 for Jenkins CI
- [x] Git version 2.47+ operational for local tracking

---

## 5. Artifacts & Deliverables
- ``.mvn/wrapper/maven-wrapper.properties``
- ``mvnw` and `mvnw.cmd` wrapper scripts`
- ``src/main/resources/application.properties` (server.port=8081)`

---

*Task completed and verified by:*  
**Muawiyah Sahil Narvel** | Roll No: `23108A0020`  
Date: October 15, 2026
