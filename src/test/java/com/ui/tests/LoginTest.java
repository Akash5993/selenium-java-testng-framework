package com.ui.tests;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ui.base.BaseTest;
import com.ui.pages.LoginPage;

/**
 * Login tests: one positive test, and one negative test that runs once per row of a DataProvider.
 * CONCEPTS: data-driven testing (@DataProvider), assertions live in tests (never in page objects).
 */
public class LoginTest extends BaseTest {

    // TODO 1: Extend BaseTest, so every test here gets a fresh browser on the login page (setup/teardown inherited).

    // TODO 2: Add private static final constants for the valid user: standard_user / secret_sauce.
    //         (Small, fixed test data can live in the test class. Never put it in page objects.)
    private static final String VALID_USERNAME = "standard_user";
    private static final String VALID_PASSWORD = "secret_sauce";

    // TODO 3: Positive test - a valid login lands on the products page.
    //         - Annotate with @Test. Name it after the behaviour, e.g. "validLoginShowsProductsPage".
    //         - Steps: new LoginPage() -> loginAs(...) -> getTitle()
    //         - Assert the title equals "Products".
    //         - TestNG's Assert.assertEquals takes (ACTUAL, EXPECTED) - the opposite order to JUnit!
    //         Look up: org.testng.Assert.assertEquals.
    @Test
    public void validLoginShowsProductsPage() {
        String title = new LoginPage()
                .loginAs(VALID_USERNAME, VALID_PASSWORD)
                .getTitle();

        Assert.assertEquals(title, "Products");
    }

    // TODO 4: DataProvider with the invalid-login cases. Each row = one test run = {username, password, expected error}.
    //         - Annotate with @DataProvider(name = "...") and return Object[][] (a 2D array: rows x columns).
    //         - Rows (exact messages from saucedemo):
    //             standard_user   / wrong_password -> "Epic sadface: Username and password do not match any user in this service"
    //             locked_out_user / secret_sauce   -> "Epic sadface: Sorry, this user has been locked out."
    //             ""              / secret_sauce   -> "Epic sadface: Username is required"
    //             standard_user   / ""             -> "Epic sadface: Password is required"
    //         Look up: TestNG @DataProvider, Java 2D arrays.
    @DataProvider(name = "invalidLogins")
    public Object[][] invalidLogins() {
        return new Object[][] {
                { VALID_USERNAME, "wrong_password", "Epic sadface: Username and password do not match any user in this service" },
                { "locked_out_user", VALID_PASSWORD, "Epic sadface: Sorry, this user has been locked out." },
                { "", VALID_PASSWORD, "Epic sadface: Username is required" },
                { VALID_USERNAME, "", "Epic sadface: Password is required" }
        };
    }

    // TODO 5: Negative test that uses the DataProvider - TestNG runs it once per row.
    //         - @Test(dataProvider = "<the name from TODO 4>"), with three String parameters matching the columns.
    //         - Steps: new LoginPage() -> loginExpectingError(...) -> getErrorMessage()
    //         - Assert the error equals the expected message from the row.
    @Test(dataProvider = "invalidLogins")
    public void invalidLoginShowsError(String username, String password, String expectedError) {
        String error = new LoginPage()
                .loginExpectingError(username, password)
                .getErrorMessage();

        Assert.assertEquals(error, expectedError);
    }
}
