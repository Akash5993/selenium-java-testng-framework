package com.ui.driver;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Creates a new browser (WebDriver) based on a browser name such as "chrome".
 * PATTERN: Factory - creation logic lives in one place; callers only see the WebDriver interface.
 */
public final class DriverFactory {

    // TODO 1: Make this a utility class: nobody should extend it or create an instance of it.
    //         (It only has static methods.) Look up: "utility class Java" - final class + private constructor.
    private DriverFactory() {
        // utility class - no instances
    }

    // TODO 2: Write a public static method that takes the browser name (a String) and returns a WebDriver.
    //         - Return type is the WebDriver INTERFACE, not ChromeDriver (polymorphism, abstraction).
    //         - Accept "chrome", "firefox" and "edge", ignoring upper/lower case and extra spaces.
    //         - Use a switch expression (arrow syntax) to create the matching driver.
    //         - For any other name, fail fast: throw an exception that names the bad value
    //           and lists the supported browsers.
    //         - No driver paths or System.setProperty needed: Selenium Manager (Selenium 4.6+)
    //           downloads the right driver automatically.
    //         Look up: switch expression Java 14, ChromeDriver / FirefoxDriver / EdgeDriver, Selenium Manager.

    // Layer 10: headless = no visible window, needed on CI servers like Jenkins that have no screen.
    public static WebDriver createDriver(String browser, boolean headless) {
        return switch (browser.trim().toLowerCase()) {
            case "chrome" -> new ChromeDriver(chromeOptions(headless));
            case "firefox" -> new FirefoxDriver(firefoxOptions(headless));
            case "edge" -> new EdgeDriver(edgeOptions(headless));
            default -> throw new IllegalArgumentException(
                    "Unsupported browser '" + browser + "'. Supported: chrome, firefox, edge");
        };
    }

    // Chrome's Password Manager pops up "Change your password" because saucedemo's password is in public
    // data-breach lists. The popup steals focus and breaks the checkout form, so tests turn it off.
    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        if (headless) {
            // Fixed window size: headless windows are small by default, which can hide elements
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless", "--width=1920", "--height=1080");
        }
        return options;
    }

    private static EdgeOptions edgeOptions(boolean headless) {
        EdgeOptions options = new EdgeOptions();
        if (headless) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        return options;
    }
}
