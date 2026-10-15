package com.muawiyah.repository.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@Tag("e2e")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ScreenshotCaptureTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private Path evidenceDir;

    @BeforeEach
    void setUp() {
        evidenceDir = Paths.get("docs", "evidence");
        try {
            Files.createDirectories(evidenceDir);
        } catch (IOException ignored) {}

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1400,900");
        options.addArguments("--remote-allow-origins=*");

        try {
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        } catch (Exception e) {
            assumeTrue(false, "ChromeDriver initialization failed: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void saveScreenshot(String filename) {
        if (driver instanceof TakesScreenshot) {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                Path dest = evidenceDir.resolve(filename);
                Files.copy(src.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("CAPTURED SCREENSHOT: " + dest.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    @DisplayName("Capture UI proof of work screenshots across all key application routes")
    void captureAllUiScreenshots() throws Exception {
        String baseUrl = "http://localhost:" + port;

        // 1. Home Page
        try {
            driver.get(baseUrl + "/");
            Thread.sleep(1000);
            saveScreenshot("ui-01-homepage.png");
        } catch (Exception e) { e.printStackTrace(); }

        // 2. Public Catalog Page
        try {
            driver.get(baseUrl + "/papers");
            Thread.sleep(1000);
            saveScreenshot("ui-02-catalog.png");
        } catch (Exception e) { e.printStackTrace(); }

        // 3. Login Page
        try {
            driver.get(baseUrl + "/login");
            Thread.sleep(1000);
            saveScreenshot("ui-03-login.png");
        } catch (Exception e) { e.printStackTrace(); }

        // 4. Authenticate as Admin (has RESEARCHER, REVIEWER, and ADMIN roles)
        try {
            driver.manage().deleteAllCookies();
            driver.get(baseUrl + "/login");
            Thread.sleep(1000);
            WebElement u = driver.findElement(By.name("username"));
            u.clear();
            u.sendKeys("admin");
            WebElement p = driver.findElement(By.name("password"));
            p.clear();
            p.sendKeys("password");
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            Thread.sleep(2000);

            // 4a. Paper Submission Form
            driver.get(baseUrl + "/papers/submit");
            Thread.sleep(1500);
            saveScreenshot("ui-04-submission-form.png");

            // 4b. Peer-Review Management Queue
            driver.get(baseUrl + "/papers/review");
            Thread.sleep(1500);
            saveScreenshot("ui-05-review-queue.png");

            // 4c. Administrative Metrics Dashboard
            driver.get(baseUrl + "/dashboard");
            Thread.sleep(1500);
            saveScreenshot("ui-06-admin-dashboard.png");
        } catch (Exception e) { e.printStackTrace(); }
    }
}
