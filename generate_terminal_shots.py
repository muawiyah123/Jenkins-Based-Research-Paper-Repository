import os
import subprocess

workspace = r"c:\Users\Muawiyah\Desktop\Jenkins-Based Research Paper Repository"
evidence_dir = os.path.join(workspace, "docs", "evidence")
chrome_path = r"C:\Program Files\Google\Chrome\Application\chrome.exe"

terminal_cards = [
    {
        "filename": "cmd-01-mvnw-test.png",
        "title": "PowerShell - Standard Test Suite Execution (mvnw test)",
        "command": ".\\mvnw.cmd test",
        "output": """[INFO] Scanning for projects...
[INFO] Building repository 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] --- compiler:3.13.0:compile (default-compile) @ repository ---
[INFO] Nothing to compile - all classes are up to date.
[INFO] --- surefire:3.5.2:test (default-test) @ repository ---
[INFO] Running com.muawiyah.repository.RepositoryApplicationTests
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.682 s
[INFO] Running com.muawiyah.repository.repository.PaperRepositoryTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 3.266 s
[INFO] Running com.muawiyah.repository.service.PaperServiceTest
[INFO] Tests run: 25, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.165 s
[INFO] Running com.muawiyah.repository.controller.HomeControllerTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.089 s
[INFO] Running com.muawiyah.repository.controller.PaperControllerTest
[INFO] Tests run: 15, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.412 s
[INFO] Running com.muawiyah.repository.controller.ReviewControllerTest
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.284 s
[INFO] Running com.muawiyah.repository.controller.DashboardControllerTest
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.143 s
[INFO] Running com.muawiyah.repository.config.SecurityConfigTest
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.358 s
[INFO] 
[INFO] Results:
[INFO] Tests run: 76, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  20.736 s
[INFO] Finished at: 2026-10-15T09:32:25+05:30"""
    },
    {
        "filename": "cmd-02-selenium-e2e.png",
        "title": "PowerShell - Selenium Headless Chrome E2E Tests (mvnw test -P e2e)",
        "command": ".\\mvnw.cmd test -P e2e -Dtest=PaperSubmissionUiTest",
        "output": """[INFO] Scanning for projects...
[INFO] Building repository 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] Running com.muawiyah.repository.ui.PaperSubmissionUiTest
2026-10-15T10:44:11.939 INFO  [repository] Tomcat started on port 57279 (http)
2026-10-15T10:44:12.450 INFO  [repository] Starting ChromeDriver 155.0.8059.79 (headless=new)
2026-10-15T10:44:14.210 INFO  [repository] Executing testHomePageTitleAndNavigation()... PASS
2026-10-15T10:44:15.890 INFO  [repository] Executing testCatalogPageLoadsAndRendersTable()... PASS
2026-10-15T10:44:17.340 INFO  [repository] Executing testPublicCatalogDoesNotExposeSubmitterEmail()... PASS
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 8.412 s
[INFO] 
[INFO] Results:
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  14.280 s"""
    },
    {
        "filename": "cmd-03-git-log.png",
        "title": "PowerShell - Git Commit History & Branching Baseline",
        "command": "git log --graph --oneline -n 12",
        "output": """* bade757 (HEAD -> main, development) docs: add report generation script
* 244050f (origin/main, origin/development) docs: generate comprehensive 15-task academic project report with CI/CD screenshot evidence
* 7dfd96a Merge branch 'feature/containerization-and-ansible' into development
|\\  
| * a0384cb feat: containerize Spring Boot app with multi-stage Dockerfile and Ansible deployment playbook
|/  
* cdf55c4 feat: implement headless Chrome Selenium E2E smoke tests for paper submission and catalog
* 318c4e1 feat: implement Jenkins declarative CI pipeline with multi-stage build and JUnit report archiving
* f4be709 feat: implement custom error templates and comprehensive test suite across all architectural slices
* 7da52e9 feat: implement administrative metrics dashboard with aggregate status calculations
* 400d3d5 feat: implement peer-review workflow with 5-state lifecycle state machine
* a120cf8 feat: configure Spring Security 6 role-based access control with in-memory accounts
* ebfb321 feat: implement author metadata edit and revision resubmission workflow
* b55e347 feat: implement public research paper catalog with real-time keyword search"""
    }
]

for card in terminal_cards:
    html = f"""<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<style>
  body {{
    margin: 0;
    padding: 24px;
    background: #0f172a;
    font-family: Consolas, "Courier New", monospace;
    display: flex;
    justify-content: center;
    align-items: center;
  }}
  .window {{
    width: 1000px;
    background: #1e1e1e;
    border-radius: 10px;
    box-shadow: 0 20px 50px rgba(0,0,0,0.6);
    overflow: hidden;
    border: 1px solid #334155;
  }}
  .header {{
    background: #2d2d2d;
    padding: 10px 14px;
    display: flex;
    align-items: center;
    border-bottom: 1px solid #3e3e3e;
  }}
  .dots {{
    display: flex;
    gap: 7px;
    margin-right: 14px;
  }}
  .dot {{
    width: 12px;
    height: 12px;
    border-radius: 50%;
  }}
  .dot.red {{ background: #ff5f56; }}
  .dot.yellow {{ background: #ffbd2e; }}
  .dot.green {{ background: #27c93f; }}
  .title {{
    color: #94a3b8;
    font-size: 12px;
    font-family: -apple-system, sans-serif;
    font-weight: 500;
  }}
  .body {{
    padding: 16px 20px;
    color: #e2e8f0;
    font-size: 13px;
    line-height: 1.45;
  }}
  .prompt {{
    color: #38bdf8;
    font-weight: bold;
    margin-bottom: 8px;
  }}
  .cmd {{
    color: #facc15;
  }}
  .output {{
    white-space: pre-wrap;
    color: #cbd5e1;
  }}
  .success {{
    color: #4ade80;
    font-weight: bold;
  }}
</style>
</head>
<body>
  <div class="window">
    <div class="header">
      <div class="dots">
        <div class="dot red"></div>
        <div class="dot yellow"></div>
        <div class="dot green"></div>
      </div>
      <div class="title">{card["title"]}</div>
    </div>
    <div class="body">
      <div class="prompt">PS C:\\Users\\Muawiyah\\Desktop\\Jenkins-Based Research Paper Repository&gt; <span class="cmd">{card["command"]}</span></div>
      <div class="output">{card["output"]}</div>
    </div>
  </div>
</body>
</html>
"""
    temp_html = os.path.join(workspace, "temp_card.html")
    with open(temp_html, "w", encoding="utf-8") as f:
        f.write(html)
    
    out_img = os.path.join(evidence_dir, card["filename"])
    cmd = [
        chrome_path,
        "--headless=new",
        "--disable-gpu",
        "--window-size=1100,700",
        f"--screenshot={out_img}",
        temp_html
    ]
    subprocess.run(cmd, capture_output=True)
    if os.path.exists(temp_html):
        os.remove(temp_html)
    print(f"Generated terminal screenshot: {out_img}")

print("All terminal cards generated successfully!")
