# UI Automation Framework: Selenium + Java + TestNG

A UI test automation framework for [saucedemo.com](https://www.saucedemo.com), built layer by layer using common enterprise patterns: Page Object Model, Factory, Singleton, ThreadLocal driver management, data-driven tests, listeners and parallel execution.

## Tech stack

| | |
|---|---|
| Language | Java 17 |
| Browser automation | Selenium 4.25 (drivers downloaded automatically by Selenium Manager) |
| Test framework | TestNG 7.10 |
| Build and run | Maven, Surefire using `testng.xml` |

## Running the tests

```bash
mvn test
```

This runs the whole suite from `testng.xml`: 6 tests, in parallel on 3 threads, in Chrome.

Any setting can be overridden from the command line, for example:

```bash
mvn test -Dheadless=true -Dbrowser=firefox
```

Settings are in `src/test/resources/config.properties`:

```properties
browser=chrome          # chrome | firefox | edge
base.url=https://www.saucedemo.com
timeout.seconds=10      # explicit wait timeout
headless=false          # true on CI servers with no screen
```

## Continuous integration (Jenkins)

The `Jenkinsfile` defines the pipeline as code:

1. Polls GitHub for new commits (every ~5 minutes).
2. Runs `mvn clean test -Dheadless=true` (headless Chrome, since CI has no screen).
3. Publishes the TestNG results (JUnit XML), so each build shows passed/failed tests and a trend chart.
4. Archives failure screenshots from `target/screenshots/`.

Failed tests mark the build **UNSTABLE** (yellow) rather than FAILED (red), so the test report is always published.

## Project structure

```
src/main/java/com/ui
├── config/ConfigReader.java     Singleton: reads config.properties once, fails fast on missing or invalid keys
├── driver/DriverFactory.java    Factory: browser name -> WebDriver (ChromeOptions disable password popups)
├── driver/DriverManager.java    ThreadLocal<WebDriver>: one browser per test thread
└── pages/
    ├── BasePage.java            Abstract parent: explicit-wait helpers (click, type, getText, isDisplayed)
    ├── LoginPage.java           \
    ├── InventoryPage.java        |  Page objects: private locators, business methods,
    ├── CartPage.java             |  fluent chaining (each method returns the next page)
    └── CheckoutPage.java        /

src/test/java/com/ui
├── base/BaseTest.java           @BeforeMethod: fresh browser per test, @AfterMethod: always quit
├── tests/
│   ├── LoginTest.java           Valid login + 4 invalid logins from a @DataProvider
│   └── CheckoutTest.java        End-to-end: login -> add to cart -> checkout -> confirmation
└── listeners/
    ├── ScreenshotListener.java  ITestListener: saves a screenshot when a test fails
    ├── RetryAnalyzer.java       IRetryAnalyzer: retries a failed test once
    └── RetryTransformer.java    IAnnotationTransformer: applies the retry to every test
```

## Design patterns and principles

- **Page Object Model:** one class per page; tests never touch locators.
- **Factory:** browser creation lives in one place.
- **Singleton:** one shared, read-only configuration.
- **ThreadLocal:** each parallel thread has its own WebDriver.
- **Observer:** TestNG listeners react to test events without the tests knowing.
- **Fluent interface:** page methods return the next page object.
- **Principles:** SOLID, DRY, encapsulation, fail fast, explicit waits only (no `Thread.sleep`), independent tests.

## Reports

| Output | Location |
|---|---|
| TestNG HTML report | `target/surefire-reports/index.html` |
| One-page summary | `target/surefire-reports/emailable-report.html` |
| Failure screenshots | `target/screenshots/` |
