package ui.tests;

import framework.config.ConfigManager;
import org.junit.jupiter.api.Test;
import ui.base.BaseTest;
import ui.pages.HomePage;
import ui.pages.LoginPage;
import ui.pages.TransactionPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionCreationTest extends BaseTest {

    @Test
    void userCanCreateTransaction() {
        LoginPage loginPage = new LoginPage();
        loginPage.init(page);

        loginPage.openLoginPage(ConfigManager.get("base.url"));

        //1. Login 
        HomePage homePage = loginPage.login(
            ConfigManager.get("login.username"),
            ConfigManager.get("login.password")
        );
        assertTrue( homePage.isHomePageVisible(),
            "Transaction list should be visible after successful login"
        );
        assertTrue( homePage.isTransactionButtonVisible(),
            "Transaction button should be visible after successful login"
        );

        //2. Create a new transaction from New (green button)
        TransactionPage transactionPage = homePage.navigateToTransactionPage();

        assertTrue( transactionPage.isTransactionPageVisible(),
            "Transaction page should be visible"
        );
        //3. Select an user.
        //4. Add amount and Note > Pay 
        String amount = "100";
        String note = "Test note";

        transactionPage.createTransaction(amount, note);

        //5. Navigate to Home
        homePage.navigateToHome();

        //6. Select Mine tab
        homePage.navigateToMineTab();

        //7. Find the transaction created before
        var  transaction = page.getByText(note).first();

        //8. Open Transaction Detail
        transaction.click();

        //9. Validate that transaction is there
        assertTrue( page.getByText(note).isVisible(),
            "Transaction note should be visible in transaction detail"
        );
        assertTrue( page.getByText("$" + amount + ".00").isVisible(),
            "Transaction amount should be visible in transaction detail"
        );

    }
}