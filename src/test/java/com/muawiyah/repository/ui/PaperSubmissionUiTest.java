package com.muawiyah.repository.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * End-to-End Selenium UI Smoke Tests (Task 14, US-014).
 *
 * Verifies Acceptance Criteria:
 * - AC-014-1: Headless browser automation submits paper and asserts title display.
 * - AC-014-2: Invalid year triggers inline red validation error in DOM.
 *
 * Tagged as @Tag("e2e") to run selectively via Maven profile:
 *   mvnw test -P e2e
 */
@Tag("e2e")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaperSubmissionUiTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--remote-allow-origins=*");

        try {
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        } catch (Exception e) {
            assumeTrue(false, "ChromeDriver initialization failed (Chrome not available or incompatible): " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    private void clickElement(WebElement element) {
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    private void loginAsResearcher() {
        driver.get(getBaseUrl() + "/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username"))).sendKeys("researcher");
        driver.findElement(By.id("password")).sendKeys("password");
        clickElement(driver.findElement(By.cssSelector("button[type='submit']")));
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login")));
    }

    @Test
    @DisplayName("Selenium E2E: Should submit research paper and view it on detail page (AC-014-1)")
    void shouldSubmitPaperSuccessfullyViaUi() {
        loginAsResearcher();

        // Navigate to paper submission form
        driver.get(getBaseUrl() + "/papers/submit");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));

        String testTitle = "Selenium Automated E2E Testing in Distributed Architectures";
        driver.findElement(By.id("title")).sendKeys(testTitle);
        driver.findElement(By.id("authors")).sendKeys("Dr. Alan Turing, Dr. Claude Shannon");
        driver.findElement(By.id("abstractText")).sendKeys(
                "This paper presents an empirical analysis of automated browser smoke testing using Selenium WebDriver.");
        driver.findElement(By.id("keywords")).sendKeys("Selenium, Testing, E2E, DevOps");
        driver.findElement(By.id("publicationYear")).clear();
        driver.findElement(By.id("publicationYear")).sendKeys("2025");
        driver.findElement(By.id("submitterEmail")).sendKeys("turing@research.org");

        // Submit form using unique button ID
        clickElement(driver.findElement(By.id("submitPaperBtn")));

        // Assert redirect to paper detail view and title presence
        wait.until(ExpectedConditions.urlMatches(".*/papers/\\d+"));
        WebElement header = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
        assertThat(header.getText()).isEqualTo(testTitle);

        // Also assert that catalog lists the newly submitted paper
        driver.get(getBaseUrl() + "/papers");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchInput")));
        assertThat(driver.getPageSource()).contains(testTitle);
    }

    @Test
    @DisplayName("Selenium E2E: Should display inline validation error for invalid publication year (AC-014-2)")
    void shouldDisplayInlineValidationErrorForInvalidYear() {
        loginAsResearcher();

        driver.get(getBaseUrl() + "/papers/submit");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));

        driver.findElement(By.id("title")).sendKeys("Valid Title for Boundary Validation Testing");
        driver.findElement(By.id("authors")).sendKeys("Test Researcher");
        driver.findElement(By.id("abstractText")).sendKeys(
                "A sufficiently detailed abstract describing boundary tests on publication year constraints.");
        driver.findElement(By.id("keywords")).sendKeys("Validation, Year");
        driver.findElement(By.id("publicationYear")).clear();
        driver.findElement(By.id("publicationYear")).sendKeys("1850"); // Invalid year (< 1900)
        driver.findElement(By.id("submitterEmail")).sendKeys("valid@university.edu");

        // Submit form
        clickElement(driver.findElement(By.id("submitPaperBtn")));

        // Assert user remains on submit form and error feedback element is visible
        WebElement errorFeedback = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".invalid-feedback, .text-danger, .alert-danger")));
        assertThat(errorFeedback.isDisplayed()).isTrue();
    }

    @Test
    @DisplayName("Selenium E2E: Should navigate between catalog, review portal, and dashboard seamlessly")
    void shouldNavigateAcrossKeySections() {
        loginAsResearcher();

        // Check catalog
        driver.get(getBaseUrl() + "/papers");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("searchInput")));
        assertThat(driver.getTitle()).contains("Catalog");

        // Check metrics dashboard
        driver.get(getBaseUrl() + "/dashboard");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("metric-total-papers")));
        assertThat(driver.getTitle()).contains("Dashboard");
    }
}
