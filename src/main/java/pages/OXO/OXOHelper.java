package pages.OXO;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.testng.Assert;

import com.microsoft.playwright.Page;

import io.qameta.allure.Allure;

public class OXOHelper {

	private Page page;
	private Map<String, Map<String, String>> testDataSections;

	public OXOHelper(Page page, Map<String, Map<String, String>> testDataSections) {
		this.page = page;
		this.testDataSections = testDataSections;
	}

	public void aceept_Cookies() {
		page.click("//button[text()='Accept All']");
	}

	public void decline_Cookies() {
		try {
			page.click("//button[text()='Acceptting cookies']");

			// Optional: Wait or verify cookie popup is dismissed
			boolean isCookiePopupGone = page.isHidden("//button[text()='Acceptting cookies']");
			Assert.assertTrue(isCookiePopupGone, "✅ Cookie popup declined successfully.");

		} catch (Exception e) {
			try {
				// ✅ Take screenshot and attach to Allure
				byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
				Allure.addAttachment("Failure Screenshot", "image/png", new ByteArrayInputStream(screenshot), ".png");
				System.out.println("📸 Screenshot captured and attached to Allure.");
			} catch (Exception ex) {
				System.out.println("⚠️ Could not capture screenshot: " + ex.getMessage());
			}

			// ❌ Fail the test with message
			Assert.fail("❌ Failed to decline cookies: " + e.getMessage());
		}
	}

	public void login(String sectionName) {

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
