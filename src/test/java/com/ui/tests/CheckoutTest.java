package com.ui.tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.ui.base.BaseTest;
import com.ui.pages.CartPage;
import com.ui.pages.InventoryPage;
import com.ui.pages.LoginPage;

/**
 * End-to-end test: the full purchase journey through all four page objects, chained fluently.
 * CONCEPTS: fluent chaining in a real test, checkpoint assertions along the way.
 */
public class CheckoutTest extends BaseTest {

    // TODO 1: Extend BaseTest.

    private static final String BACKPACK = "Sauce Labs Backpack";
    private static final String BOLT_T_SHIRT = "Sauce Labs Bolt T-Shirt";

    // TODO 2: One @Test for the whole journey, e.g. "userCanCompleteCheckout". Steps and checkpoints:
    //         1. Log in as standard_user / secret_sauce          -> InventoryPage
    //         2. Add "Sauce Labs Backpack" and "Sauce Labs Bolt T-Shirt" (addToCart returns this, so chain them)
    //         3. ASSERT the cart count is 2
    //         4. Go to the cart                                   -> CartPage
    //         5. ASSERT the cart's item names equal List.of(<the two products>)
    //         6. Checkout -> enterDetails(any first name, last name, postal code) -> finish
    //         7. ASSERT the confirmation message is "Thank you for your order!"
    //         - Hard assertions are right here: if the cart is wrong, there's no point continuing the checkout.
    //         Look up: List.of (Java 9), comparing two Lists with assertEquals.
    @Test
    public void userCanCompleteCheckout() {
        InventoryPage inventory = new LoginPage()
                .loginAs("standard_user", "secret_sauce")
                .addToCart(BACKPACK)
                .addToCart(BOLT_T_SHIRT);
        Assert.assertEquals(inventory.getCartCount(), 2, "Cart badge count");

        CartPage cart = inventory.goToCart();
        Assert.assertEquals(cart.getItemNames(), List.of(BACKPACK, BOLT_T_SHIRT), "Items in the cart");

        String confirmation = cart.checkout()
                .enterDetails("Akash", "Tester", "12345")
                .finish()
                .getConfirmationMessage();
        Assert.assertEquals(confirmation, "Thank you for your order!");
    }
}
