package com.ui.driver;

import org.openqa.selenium.WebDriver;

/**
 * Holds the current test thread's browser so parallel tests never share one.
 * PATTERN: ThreadLocal storage - one WebDriver per thread, behind static methods.
 */
public final class DriverManager {

    // TODO 1: Make this a utility class: final class + private constructor (static methods only).
    private DriverManager() {
        // utility class - no instances
    }

    // TODO 2: Add a private static final ThreadLocal field that holds a WebDriver.
    //         Each thread sees only the driver it stored itself.
    //         Look up: ThreadLocal set / get / remove.
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    // TODO 3: Write a public static method that stores the given WebDriver for the current thread.
    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    // TODO 4: Write a public static method that returns the current thread's WebDriver.
    //         - If there is none, fail fast with a clear message
    //           (it means the driver was never set, or it's being used from a different thread).
    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "No WebDriver for thread '" + Thread.currentThread().getName()
                            + "'. Was setDriver() called in the test setup?");
        }
        return driver;
    }

    // TODO 5: Write a public static method that closes the browser and cleans up.
    //         - If a driver exists, call quit() on it (not close() - look up the difference).
    //         - ALWAYS remove the value from the ThreadLocal afterwards, even if quit() throws,
    //           so a reused thread never picks up a dead driver (and there's no memory leak).
    //         Look up: try / finally, driver.quit() vs driver.close().
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }
}
