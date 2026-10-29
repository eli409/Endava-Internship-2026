package ui.tests;

import framework.config.ConfigManager;
import org.junit.jupiter.api.Test;
import ui.base.BaseTest;
import ui.pages.HomePage;
import ui.pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class NegativeLoginTest extends BaseTest {

    @Test
    void userCannotLoginWithInvalidCredentials() {
        LoginPage loginPage = new LoginPage();
        loginPage.init(page);

        loginPage.openLoginPage(ConfigManager.get("base.url"));

        //1. Login with invalid credentials
        loginPage.enterUsername("invalid_usernam");
        loginPage.enterPassword("invalid_password");
        loginPage.clickLogin();

        //2. Validate that the user is not logged in and an error message is displayed
        assertTrue( loginPage.isErrorMessageVisible(),
                "Error message should be visible after failed login attempt"
        );

        assertTrue( page.url().contains("/signin"),
                "User should remain on the login page after failed login attempt"
        );

        assertFalse( page.locator("[data-test='transaction-list']").isVisible(),
                "Transaction list should not be visible after failed login attempt"
        );
    }
}