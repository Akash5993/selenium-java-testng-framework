package com.ui.pages;

import org.openqa.selenium.By;

/**
 * Saucedemo's checkout: your details -> overview -> confirmation.
 * DESIGN CHOICE: one class for the three short checkout screens to keep things simple.
 * (A stricter design would use three page classes - mention this trade-off if asked.)
 */
public class CheckoutPage extends BasePage {

    // TODO 1: Locators for all three checkout screens.
    private static final By FIRST_NAME = By.cssSelector("[data-test='firstName']");
    private static final By LAST_NAME = By.cssSelector("[data-test='lastName']");
    private static final By POSTAL_CODE = By.cssSelector("[data-test='postalCode']");
    private static final By CONTINUE_BUTTON = By.cssSelector("[data-test='continue']");
    private static final By FINISH_BUTTON = By.cssSelector("[data-test='finish']");
    private static final By COMPLETE_HEADER = By.cssSelector("[data-test='complete-header']");

    // TODO 2: Screen 1 - fill in the buyer's details and continue to the overview.
    //         - Returns this: the next screen is still part of this class.
    public CheckoutPage enterDetails(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        click(CONTINUE_BUTTON);
        return this;
    }

    // TODO 3: Screen 2 - place the order.
    public CheckoutPage finish() {
        click(FINISH_BUTTON);
        return this;
    }

    // TODO 4: Screen 3 - the confirmation text ("Thank you for your order!"). The test asserts on it.
    public String getConfirmationMessage() {
        return getText(COMPLETE_HEADER);
    }
}
