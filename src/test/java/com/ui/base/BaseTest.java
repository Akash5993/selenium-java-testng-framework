package com.ui.base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.ui.config.ConfigReader;
import com.ui.driver.DriverFactory;
import com.ui.driver.DriverManager;

/**
 * Parent of every test class: opens a fresh browser before each test and closes it after.
 * CONCEPTS: SRP (tests hold only steps + assertions), inheritance, TestNG lifecycle annotations.
 * This is where ConfigReader, DriverFactory and DriverManager finally connect.
 */
public abstract class BaseTest {

    // TODO 1: Make this class abstract - it has no tests of its own; test classes extend it.
    //         (Abstract with NO abstract methods is allowed: here it only means "can't be instantiated".)

    // TODO 2: Write the setup method that runs BEFORE EACH test method.
    //         - Annotate it so TestNG runs it before every @Test (alwaysRun = true, so it also runs
    //           when tests are filtered by groups). Look up: @BeforeMethod vs @BeforeClass.
    //         - Steps (the same flow as the diagram):
    //             1. read the browser name from ConfigReader (key: browser)
    //             2. create the browser with DriverFactory
    //             3. store it for this thread with DriverManager
    //             4. open the base URL from ConfigReader (key: base.url) using the stored driver
    //         - Public method, returns nothing.
    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ConfigReader config = ConfigReader.getInstance();
        String browser = config.getString("browser");
        DriverManager.setDriver(DriverFactory.createDriver(browser, config.getBoolean("headless")));
        DriverManager.getDriver().get(config.getString("base.url"));
    }

    // TODO 3: Write the teardown method that runs AFTER EACH test method.
    //         - Annotate it with alwaysRun = true, so the browser is closed even when the test FAILED.
    //         - One step: ask DriverManager to quit the browser (it also removes it from the ThreadLocal).
    //         Look up: @AfterMethod(alwaysRun = true).
    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
