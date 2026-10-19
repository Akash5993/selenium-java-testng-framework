package com.ui.pages;

import org.openqa.selenium.By;

/**
 * The products page shown after a successful login.
 * CONCEPTS: dynamic locators, fluent chaining (return this / the next page), findElements for optional elements.
 */
public class InventoryPage extends BasePage {

    // TODO 1: Fixed locators for this page (private static final constants).
    private static final By TITLE = By.cssSelector("[data-test='title']");
    private static final By CART_LINK = By.cssSelector("[data-test='shopping-cart-link']");
    private static final By CART_BADGE = By.cssSelector("[data-test='shopping-cart-badge']");

    // TODO 2: Page title text ("Products") - tests use it to check that login really worked.
    public String getTitle() {
        return getText(TITLE);
    }

    // TODO 3: Add a product by its visible name - a DYNAMIC locator built at runtime.
    //         - Every product has its own button: data-test="add-to-cart-<name in lower case, spaces as dashes>"
    //           e.g. "Sauce Labs Backpack" -> add-to-cart-sauce-labs-backpack.
    //         - One method works for all products instead of one locator constant per product.
    //         - Returns this: the browser stays on this page, so the test can add more or go to the cart.
    //         Look up: String.toLowerCase, String.replaceAll with the regex \\s+ (one or more spaces).
    public InventoryPage addToCart(String productName) {
        String id = "add-to-cart-" + productName.toLowerCase().replaceAll("\\s+", "-");
        click(By.cssSelector("[data-test='" + id + "']"));
        return this;
    }

    // TODO 4: Number shown on the cart icon.
    //         - The badge DOESN'T EXIST when the cart is empty, so isDisplayed/getText would wait the full timeout.
    //         - findElements (plural) returns an empty list instead of throwing, and returns immediately.
    //         Look up: findElement vs findElements, Integer.parseInt.
    public int getCartCount() {
        var badges = driver.findElements(CART_BADGE);
        return badges.isEmpty() ? 0 : Integer.parseInt(badges.get(0).getText());
    }

    // TODO 5: Go to the cart - the browser moves, so return the NEXT page object.
    public CartPage goToCart() {
        click(CART_LINK);
        return new CartPage();
    }
}
