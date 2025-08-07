package pages.Drybar;

import java.util.Map;

import org.testng.Assert;

import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import utils.ScreenshotUtils;
import utils.waitUtils;

public class DrybarHelper {

	private Page page;
	private Map<String, Map<String, String>> testDataSections;

	public DrybarHelper(Page page, Map<String, Map<String, String>> testDataSections) {
		this.page = page;
		this.testDataSections = testDataSections;
	}

	public void acceptCookies() {
		try {
			waitUtils.waitUntilElementVisible(page, "//div[@id='header']");
			waitUtils.waitUntilElementVisible(page, "//form[@aria-label='Subscribe to Newsletter']");
			page.locator("//button[@id='truste-consent-button']").click();
		} catch (Exception e) {
			System.out.println("❌ Failed to accept cookies: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "acceptCookies");
			Assert.fail("Failed to accept cookies:");
		}
	}

	public void Close_Popup() throws Exception {
		Thread.sleep(4000);
		Locator closeButton = page.locator("//div[@id='ltkpopup-close-button']");
		if (closeButton.isVisible()) {
			System.out.println("Popup appeared. Closing it...");
			closeButton.click();
		} else {
			System.out.println("Popup not shown. Proceeding...");
		}
	}

	public void addToCart() {
		try {
			waitUtils.waitUntilElementVisible(page, "(//button[@title='ADD TO BAG'])[1]");
			page.locator("(//button[@title='ADD TO BAG'])[1]").click();
			Close_Popup();
			waitUtils.waitUntilElementEnabled(page,
					"//div[@class='cart-drawer__totals-content-checkout relative grid bg-white']");
			page.locator("//div[@class='cart-drawer__totals-content-checkout relative grid bg-white']").click();
			waitUtils.waitUntilPageIsReady(page);
			waitUtils.waitUntilElementVisible(page, "//input[@name='email_address']");
			waitUtils.validateCurrentUrl(page, "drybar.com/checkout/");

		} catch (Exception e) {
			System.out.println("❌ Failed to add to cart: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "addToCart");
			Assert.fail("Add to cart/Checkout failed or buttons are not visible/enabled");
		}
	}

	public void login(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);

			page.waitForSelector("//button[@id='customer-menu']");
			page.click("//button[@id='customer-menu']");
			page.waitForSelector("//a[@title='Sign In']");
			page.click("//a[@title='Sign In']");
			waitUtils.waitUntilElementVisible(page, "//span[text()='Sign In']");
			page.fill("//input[@id='email']", data.get("Email"));
			page.fill("//input[@id='pass']", data.get("Password"));
			page.click("//span[text()='Sign In']");
			waitUtils.waitUntilPageIsReady(page);

		} catch (Exception | AssertionError e) {
			System.out.println("❌ Login failed: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "login");
			Assert.fail("Login failed");
		}
	}

	public void searchProduct(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);

			waitUtils.waitUntilElementVisible(page, "//button[@aria-label='Open Search']");
			page.click("//button[@aria-label='Open Search']");
			String productName = data.get("SimpleProduct");
			System.out.println("🔍 Searching for product: " + productName);
			page.fill("//input[@type='search']", productName);
			page.keyboard().press("Enter");
			page.waitForSelector("//ol[@class='ais-InfiniteHits-list']");
			waitUtils.validateCurrentUrl(page, "/catalogsearch/");

		} catch (Exception e) {
			System.out.println("❌ Failed to search product: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "searchProduct");
			Assert.fail("Product Serach is failed");
		}
	}

	public void fillShippingAddress(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);

			waitUtils.waitUntilPageIsReady(page);
			waitUtils.waitUntilElementEnabled(page, "//input[@name='email']");
			waitUtils.waitUntilElementVisible(page, "//section[@id='quote-summary']");
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
			Assert.fail("Failed to fill shipping address");
		}
	}

	public void fillPaymentDetails(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);

			waitUtils.waitUntilPageIsReady(page);
			waitUtils.waitUntilElementEnabled(page, "//div[contains(@class,'checkout-main-details__place-order')]");
			FrameLocator paymentFrame = page.frameLocator("//iframe[@title='Secure payment input frame']");
			paymentFrame.locator("#Field-numberInput").fill(data.get("CardNumber"));
			paymentFrame.locator("#Field-expiryInput").fill(data.get("ExpDate"));
			paymentFrame.locator("#Field-cvcInput").fill(data.get("CVV"));
			page.click("//fieldset[contains(@class,'checkout-coupon-code')]");
		} catch (Exception e) {
			System.out.println("❌ Failed to fill payment details: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "fillPaymentDetails");
			Assert.fail("Failed to fill payment details");
		}
	}

	public boolean isLoginSuccessful() {
		try {
			return page.locator("//a[@title='My Account']").isVisible();
		} catch (Exception e) {
			System.out.println("❌ Failed to verify login: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "isLoginSuccessful");
			Assert.fail("Failed to verify Login");
			return false;
		}
	}
}