package pages.Drybar;

import java.util.Map;

import org.testng.Assert;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import utils.ScreenshotUtils;

public class DrybarHelper {

	private Page page;
	private Map<String, Map<String, String>> testDataSections;

	public DrybarHelper(Page page, Map<String, Map<String, String>> testDataSections) {
		this.page = page;
		this.testDataSections = testDataSections;
	}

	public void acceptCookies() {
		try {
			page.locator("//button[@id='truste-consent-button']").click();
		} catch (Exception e) {
			System.out.println("❌ Failed to accept cookies: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "acceptCookies");
		}
	}

	public void addToCart() {
		try {
			page.waitForSelector("(//button[@title='ADD TO BAG'])[1]");
			page.locator("(//button[@title='ADD TO BAG'])[1]").click();
			page.waitForTimeout(4000);

			Locator closeButton = page.locator("//div[@id='ltkpopup-close-button']");
			if (closeButton.isVisible()) {
				System.out.println("Popup appeared. Closing it...");
				closeButton.click();
			} else {
				System.out.println("Popup not shown. Proceeding...");
			}

			page.waitForSelector("//div[@class='cart-drawer__totals-content-checkout relative grid bg-white']");
			page.locator("//div[@class='cart-drawer__totals-content-checkout relative grid bg-white']").click();
			page.waitForTimeout(5000);
		} catch (Exception e) {
			System.out.println("❌ Failed to add to cart: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "addToCart");
		}
	}

	public void login(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);

			page.waitForSelector("//button[@id='customer-menu']");
			page.click("//button[@id='customer-menu']");
			page.waitForSelector("//a[@title='Sign In']");
			page.click("//a[@title='Sign In']");
			page.waitForSelector("//div[@id='customer-login-container']");

			page.fill("//input[@id='email']", data.get("Email"));
			page.fill("//input[@id='pass']", data.get("Password"));
			page.click("//span[text()='Sign In']");

		} catch (Exception | AssertionError e) {
			System.out.println("❌ Login failed: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "login");
			Assert.fail("Login failed or SignOut button not visible/enabled");
		}
	}

	public void searchProduct(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);
			if (data == null)
				throw new RuntimeException("Product data section not found: " + sectionName);

			page.waitForSelector("//button[@aria-label='Open Search']");
			page.click("//button[@aria-label='Open Search']");
			String productName = data.get("SimpleProduct");
			System.out.println("🔍 Searching for product: " + productName);

			page.fill("//input[@type='search']", productName);
			page.keyboard().press("Enter");
			page.waitForSelector("//ol[@class='ais-InfiniteHits-list']");
		} catch (Exception e) {
			System.out.println("❌ Failed to search product: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "searchProduct");
		}
	}

	public void fillShippingAddress(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);

			page.fill("//input[@name='email_address']", data.get("ShippingEmail"));
			page.fill("//input[@name='firstname']", data.get("FirstName"));
			page.fill("//input[@name='lastname']", data.get("LastName"));
			page.fill("//input[@name='street[0]']", data.get("Street"));
			page.fill("//input[@name='city']", data.get("City"));
			page.selectOption("//select[@name='region']", data.get("State"));
			page.fill("//input[@name='postcode']", data.get("ZipCode"));
			page.fill("//input[@name='telephone']", data.get("Phone"));

			System.out.println("✅ Shipping address filled.");
		} catch (Exception e) {
			System.out.println("❌ Failed to fill shipping address: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "fillShippingAddress");
		}
	}

	public void fillPaymentDetails(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);
			if (data == null)
				throw new RuntimeException("Payment section not found: " + sectionName);

			FrameLocator paymentFrame = page.frameLocator("//iframe[@title='Secure payment input frame']");

			paymentFrame.locator("#Field-numberInput").fill(data.get("CardNumber"));
			paymentFrame.locator("#Field-expiryInput").fill(data.get("ExpDate"));
			paymentFrame.locator("#Field-cvcInput").fill(data.get("CVV"));
			page.click("//fieldset[contains(@class,'checkout-coupon-code')]");
			page.waitForTimeout(10000);
		} catch (Exception e) {
			System.out.println("❌ Failed to fill payment details: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "fillPaymentDetails");
		}
	}

	public boolean isLoginSuccessful() {
		try {
			return page.locator("//a[@title='My Account']").isVisible();
		} catch (Exception e) {
			System.out.println("❌ Failed to verify login: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "isLoginSuccessful");
			return false;
		}
	}
}