package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class TransactionPage {
    private Page page;
    
    Locator userSelect;
    Locator amountInput;
    Locator noteInput;
    Locator payButton;

    public void init(Page page) {
        this.page = page;
        userSelect = page.locator("[data-test^=user-list-item]");
        amountInput = page.locator("#amount");
        noteInput = page.locator("#transaction-create-description-input");
        payButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Pay"));
    }

    public void selectUser() {
        userSelect.first().click();
    }

    public void enterAmount(String amount) {
        amountInput.fill(amount);
    }

    public void enterNote(String note) {
        noteInput.fill(note);
    }

    public void clickPayButton() {
        payButton.click();
    }

    public TransactionPage createTransaction(String amount, String note) {
        selectUser();
        enterAmount(amount);
        enterNote(note);
        clickPayButton();
        return this;
    }

    public boolean isTransactionPageVisible() {
        return userSelect.first().isVisible();
    }

}