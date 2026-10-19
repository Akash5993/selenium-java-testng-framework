package com.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ui.config.ConfigReader;
import com.ui.driver.DriverManager;

/**
 * Parent of every page object: holds the driver and a wait, and offers common
 * actions that wait first.
 * CONCEPTS: inheritance (pages extend this), DRY (waiting written once), no
 * Thread.sleep.
 */
public abstract class BasePage {

    // TODO 1: Make this class abstract - it's not a real page, so nobody should
    // create one directly.
    // Look up: abstract class vs interface.

    // TODO 2: Add two fields that subclasses can use but tests can't:
    // - the WebDriver
    // - a WebDriverWait
    // Make them final (set once in the constructor). Look up: the protected access
    // modifier.
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    // TODO 3: Write the constructor (protected - only subclasses call it).
    // - Get the driver from DriverManager (the current thread's browser).
    // - Build the WebDriverWait with that driver and the timeout from ConfigReader
    // (key: timeout.seconds - getTimeout already returns a Duration).
    // Every page's constructor runs this first, automatically. Look up: super() /
    // constructor chaining.
    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, ConfigReader.getInstance().getTimeout("timeout.seconds"));
    }

    // TODO 4: Write a protected click helper that takes a By locator.
    // - Wait until the element is CLICKABLE, then click it.
    // Look up: WebDriverWait.until, ExpectedConditions.elementToBeClickable.
    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    // TODO 5: Write a protected type helper that takes a By locator and the text.
    // - Wait until the element is VISIBLE, clear it, then type the text.
    // Look up: ExpectedConditions.visibilityOfElementLocated, WebElement.clear /
    // sendKeys.
    protected void type(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    // TODO 6: Write a protected helper that returns an element's text, given a By
    // locator.
    // - Wait until VISIBLE, then return its text.

    protected String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    // TODO 7: Write a protected helper that returns true/false: is the element
    // displayed?
    // - Wait until VISIBLE and return true; if the wait times out, return false
    // instead of failing.
    // - Note: when the element is absent, this waits the full timeout before
    // returning false.
    // Look up: TimeoutException, try / catch.
    protected boolean isDisplayed(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            // Not visible within the timeout - report it instead of failing the test
            return false;
        }
    }
}
