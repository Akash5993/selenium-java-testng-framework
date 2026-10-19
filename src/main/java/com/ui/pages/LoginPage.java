package com.ui.pages;

import org.openqa.selenium.By;

/**
 * The saucedemo login page (the base URL).
 * CONCEPTS: Page Object Model, encapsulation (private locators), fluent chaining (methods return the next page).
 */
public class LoginPage extends BasePage {

    // TODO 1: Locators - private static final By constants, one per element this page uses.
    //         - private: only this page knows them (tests use page methods, never locators).
    //         - static: the same for every LoginPage object; final: never change -> UPPER_CASE constants.
    //         - saucedemo puts a data-test attribute on elements; check each one in DevTools (F12, Ctrl+F).
    private static final By USERNAME = By.cssSelector("[data-test='username']");
    private static final By PASSWORD = By.cssSelector("[data-test='password']");
    private static final By LOGIN_BUTTON = By.cssSelector("[data-test='login-button']");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");

    // TODO 2: Successful login - name the method after what the user does, not the UI steps.
    //         - Returns InventoryPage because that's where the browser lands (fluent chaining).
    public InventoryPage loginAs(String username, String password) {
        submit(username, password);
        return new InventoryPage();
    }

    // TODO 3: Login that is expected to FAIL (wrong password, locked-out user...).
    //         - Returns this same LoginPage, because the browser stays here and the test reads the error.
    //         - Two methods for two outcomes: each return type matches where the browser really ends up.
    public LoginPage loginExpectingError(String username, String password) {
        submit(username, password);
        return this;
    }

    // TODO 4: Expose state, don't judge it. The page returns the text; the TEST asserts on it (no assertions here).
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }

    // TODO 5: Shared steps of both login methods live in one private helper (DRY).
    //         - private: tests can't call it; they must choose loginAs or loginExpectingError.
    //         - type/click come from BasePage, so every step already waits for the element.
    private void submit(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
    }
}
