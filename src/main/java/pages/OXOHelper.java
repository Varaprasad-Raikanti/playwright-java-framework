package pages;

import org.testng.Assert;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PlaywrightAssertions;

public class OXOHelper {
    private final Page page;

    public OXOHelper(Page page) {
        this.page = page;
    }
    
    public void aceept_Cookies() {
    	page.click("//button[text()='Accept All']");
    }
    
 
    public void decline_Cookies() {
        try {
            page.click("//button[text()='Acceptting cookies']");

            // Add a wait or validation to ensure the cookie popup is dismissed
            boolean isCookiePopupGone = page.isHidden("//button[text()='Acceptting cookies']");
            Assert.assertTrue(isCookiePopupGone, "✅ Cookie popup declined successfully.");

        } catch (Exception e) {
            // Log and fail the test if any step fails
            Assert.fail("❌ Failed to decline cookies: " + e.getMessage());
        }
    }


    public void enterFirstName(String firstName) {
        page.fill("#firstName", firstName);
    }

    public void enterLastName(String lastName) {
        page.fill("#lastName", lastName);
    }

    public void enterEmail(String email) {
        page.fill("#userEmail", email);
    }

    public void selectGender() {
        page.click("label[for='gender-radio-1']"); // Select "Male"
    }

    public void enterMobile(String mobile) {
        page.fill("#userNumber", mobile);
    }

    public void clickSubmit() {
        page.click("#submit");
    }
}
