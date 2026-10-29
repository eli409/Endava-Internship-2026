package ui.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MyAccountPage {

    private Page page;

    // Locators
    private Locator userSettingsText;

    public void init(Page page) {
        this.page = page;
        userSettingsText = page.getByText("User Settings");
    }

    public boolean isMyAccountPageVisible() {
        return userSettingsText.isVisible();
    }
}