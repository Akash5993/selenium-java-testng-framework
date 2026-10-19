package com.ui.listeners;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.ui.driver.DriverManager;

/**
 * Saves a screenshot of the browser whenever a test fails.
 * PATTERN: Observer - TestNG publishes test events; this listener subscribes and reacts to "test failed".
 */
public class ScreenshotListener implements ITestListener {

    private static final Path SCREENSHOT_DIR = Path.of("target", "screenshots");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    // TODO 1: Implement TestNG's ITestListener interface.
    //         - Its methods are DEFAULT methods (Java 8), so you only override the events you care about.
    //         Look up: org.testng.ITestListener, Java 8 default methods in interfaces.

    // TODO 2: Override onTestFailure(ITestResult result).
    //         - Get the current thread's driver from DriverManager.
    //           (TestNG calls this BEFORE @AfterMethod quits the browser, so the driver still exists.)
    //         - Cast it to TakesScreenshot and call getScreenshotAs(...) to get the image as bytes.
    //           WebDriver itself has no screenshot method - the browser classes also implement TakesScreenshot.
    //         - Save it under target/screenshots/ with a unique name: test method name + a timestamp,
    //           so DataProvider rows and parallel runs don't overwrite each other.
    //         - Print or log where the file was saved.
    //         Look up: TakesScreenshot, OutputType.BYTES, java.nio.file.Files.createDirectories / Files.write,
    //                  result.getMethod().getMethodName().

    // TODO 3: Never let the listener itself crash the run.
    //         - If setup failed there is no driver: skip the screenshot instead of throwing.
    //         - If saving fails (IOException), report it but don't throw.
    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        WebDriver driver;
        try {
            driver = DriverManager.getDriver();
        } catch (IllegalStateException e) {
            System.out.println("[Screenshot] No browser for " + testName + " - screenshot skipped");
            return;
        }

        try {
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Files.createDirectories(SCREENSHOT_DIR);
            Path file = SCREENSHOT_DIR.resolve(testName + "_" + LocalDateTime.now().format(TIMESTAMP) + ".png");
            Files.write(file, png);
            System.out.println("[Screenshot] " + testName + " failed - saved " + file.toAbsolutePath());
        } catch (IOException | WebDriverException e) {
            System.out.println("[Screenshot] Could not save screenshot for " + testName + ": " + e.getMessage());
        }
    }
}
