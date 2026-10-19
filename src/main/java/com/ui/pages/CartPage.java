package com.ui.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * The shopping cart page.
 * CONCEPTS: reading a list of elements with streams, fluent chaining to the checkout.
 */
public class CartPage extends BasePage {

    // TODO 1: Locators. CART_LIST is the container - it exists even when the cart is empty.
    private static final By CART_LIST = By.cssSelector("[data-test='cart-list']");
    private static final By ITEM_NAMES = By.cssSelector("[data-test='inventory-item-name']");
    private static final By CHECKOUT_BUTTON = By.cssSelector("[data-test='checkout']");

    // TODO 2: Names of all products in the cart, as a List<String> - tests check the right items were added.
    //         - First wait for the cart list, so the page has loaded before we read it.
    //         - findElements returns every match (an empty list if the cart is empty).
    //         - A stream turns List<WebElement> into List<String>: map each element to its text.
    //         Look up: Java streams map(), method reference WebElement::getText, Stream.toList() (Java 16).
    public List<String> getItemNames() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(CART_LIST));
        return driver.findElements(ITEM_NAMES).stream()
                .map(WebElement::getText)
                .toList();
    }

    // TODO 3: Start checkout - the browser moves, so return the next page object.
    public CheckoutPage checkout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutPage();
    }
}
