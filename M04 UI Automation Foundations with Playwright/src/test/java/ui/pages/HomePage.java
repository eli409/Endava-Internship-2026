package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class HomePage {

    private Page page;

    // Locators
    private Locator logoutButton;
    private Locator transactionList;
    private Locator myAccountLink;
    private Locator transactionButton;

    public void init(Page page) {
        this.page = page;
        logoutButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout"));
        transactionList = page.locator("//div[@data-test='transaction-list']");
        myAccountLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("My Account"));
        transactionList.isVisible();
        transactionButton = page.locator("[data-test='nav-top-new-transaction']");
    }

    public boolean isLogoutButtonVisible() {
        return logoutButton.isVisible();
    }

    public boolean isHomePageVisible() {
        return transactionList.isVisible();
    }

    public boolean isTransactionButtonVisible() {
        return transactionButton.isVisible();
    }

    public void clickTransactionButton() {
        transactionButton.click();
    }

    public MyAccountPage navigateToMyAccount() {
        myAccountLink.click();
        MyAccountPage myAccountPage = new MyAccountPage();
        myAccountPage.init(page);
        return myAccountPage;
    }

    public TransactionPage navigateToTransactionPage() {
        clickTransactionButton();
        TransactionPage transactionPage = new TransactionPage();
        transactionPage.init(page);
        return transactionPage;
    }

    public void navigateToHome() {
        page.locator("[data-test='sidenav-home']").click();
        transactionList.waitFor();
    }

    public void navigateToMineTab() {
        page.locator("[data-test='nav-personal-tab']").click();
        transactionList.waitFor();
    }

}