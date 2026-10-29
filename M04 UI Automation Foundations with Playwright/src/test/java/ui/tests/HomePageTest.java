package ui.tests;

import framework.config.ConfigManager;
import org.junit.jupiter.api.Test;
import ui.base.BaseTest;
import ui.pages.HomePage;
import ui.pages.LoginPage;
import ui.pages.MyAccountPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

class HomePageTest extends BaseTest {

    @Test
    void userCanNavigateToMyAccount() {
        LoginPage loginPage = new LoginPage();
        loginPage.init(page);

        loginPage.openLoginPage(ConfigManager.get("base.url"));

        HomePage homePage = loginPage.login(
                ConfigManager.get("login.username"),
                ConfigManager.get("login.password")
        );

        MyAccountPage myAccountPage = homePage.navigateToMyAccount();

        assertTrue( myAccountPage.isMyAccountPageVisible(),
                "My Account page should be visible after navigation" );
    }
}