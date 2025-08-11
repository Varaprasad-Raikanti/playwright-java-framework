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
			waitUtils.waitUntilElementEnabled(page, "//div[@id='header']");
			waitUtils.waitUntilElementEnabled(page, "//form[@aria-label='Subscribe to Newsletter']");
			page.locator("//button[@id='truste-consent-button']").click();
		} catch (Exception e) {
			System.out.println("❌ Failed to accept cookies: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "acceptCookies");
			Assert.fail("Failed to accept cookies:");
		}
	}

	public void closeGWPPopup() throws Exception {
		Thread.sleep(4000);
		Locator popup = page.locator("//div[@x-ref='freegift']");
		// Check if popup exists and is visible in DOM
		if (popup.count() > 0 && popup.isVisible()) {
			System.out.println("GWP Popup appeared. Closing it...");
			Locator closeBtn = page.locator("//div[@x-ref='freegift']//button[@aria-label='Close']");
			closeBtn.hover();
			closeBtn.click();

			System.out.println("GWP Popup closed successfully.");
		} else {
			System.out.println("GWP Popup not shown. Proceeding...");
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
			waitUtils.waitUntilPageIsReady(page);
			int size = page.locator("//input[@id='srp-search-general']").count();
			if (size > 0) {
				waitUtils.waitUntilElementEnabled(page, "//div[@id='product-card-61675']//button[@title='ADD TO BAG']");
				page.locator("//div[@id='product-card-61675']//button[@title='ADD TO BAG']").click();
				Close_Popup();
				waitUtils.waitUntilElementEnabled(page,
						"//div[@class='cart-drawer__totals-content-checkout relative grid bg-white']");
				page.locator("//div[@class='cart-drawer__totals-content-checkout relative grid bg-white']").click();
				waitUtils.waitUntilPageIsReady(page);
				waitUtils.waitUntilElementEnabled(page, "//fieldset[contains(@class,'checkout-coupon-code')]");
				waitUtils.validateCurrentUrl(page, "drybar.com/checkout/");
			} else {

				SelectCategoryProduct();
				waitUtils.waitUntilElementEnabled(page, "//div[@id='product-card-61675']");
				page.locator("//div[@id='product-card-61675']").click();
				waitUtils.waitUntilPageIsReady(page);
				Close_Popup();
				waitUtils.waitUntilElementEnabled(page, "//button[@id='product-addtocart-button']");
				page.locator("//button[@id='product-addtocart-button']").click();
				waitUtils.waitUntilElementEnabled(page,
						"//div[@class='cart-drawer__totals-content-checkout relative grid bg-white']");
				page.locator("//div[@class='cart-drawer__totals-content-checkout relative grid bg-white']").click();
				waitUtils.waitUntilPageIsReady(page);
				waitUtils.waitUntilElementEnabled(page, "//fieldset[contains(@class,'checkout-coupon-code')]");
				waitUtils.validateCurrentUrl(page, "drybar.com/checkout/");

			}

		} catch (Exception e) {
			System.out.println("❌ Failed to add to cart: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "addToCart");
			Assert.fail("Add to cart/Checkout failed or buttons are not visible/enabled");
		}
	}

	public void login(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);

			waitUtils.waitUntilElementEnabled(page, "//button[@id='customer-menu']");
			page.click("//button[@id='customer-menu']");
			page.waitForSelector("//a[@title='Sign In']");
			page.click("//a[@title='Sign In']");
			waitUtils.waitUntilElementEnabled(page, "//span[text()='Sign In']");
			page.fill("//input[@id='email']", data.get("Email"));
			page.fill("//input[@id='pass']", data.get("Password"));
			page.click("//span[text()='Sign In']");
			waitUtils.waitUntilElementEnabled(page, "//form[@aria-label='Subscribe to Newsletter']");
			waitUtils.validatePageTitle(page, "Drybar");

		} catch (Exception | AssertionError e) {
			System.out.println("❌ Login failed: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "login");
			Assert.fail("❌ Login failed");
		}
	}

	public void searchProduct(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);

			waitUtils.waitUntilElementEnabled(page, "//button[@aria-label='Open Search']");
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

	public void SelectCategoryProduct() {
		try {

			waitUtils.waitUntilElementEnabled(page,
					"//button[contains(@class,'menu-node-hair-tools')]//span[contains(text(),'Hair Tools')]");
			page.click("//button[contains(@class,'menu-node-hair-tools')]//span[contains(text(),'Hair Tools')]");
			page.waitForSelector("//span[contains(text(),'All Hair Tools')]").click();
			waitUtils.validateCurrentUrl(page, "hair-care-tools/all-hair-care-tools");

		} catch (Exception e) {
			System.out.println("❌ Failed to select Category: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "Category Select");
			Assert.fail("Category Selection is failed");
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

	public void fillRegShippingAddress(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);

			waitUtils.waitUntilPageIsReady(page);
			waitUtils.waitUntilElementEnabled(page, "//div[@x-ref='freegift']");
			closeGWPPopup();
			int size = page.locator("//button[contains(text(),'New Address')]").count();
			if (size > 0) {
				System.out.println("New Address Button is Enabled.........");

				page.click("//button[contains(text(),'New Address')]");
				page.fill("//input[@name='firstname']", data.get("FirstName"));
				page.fill("//input[@name='lastname']", data.get("LastName"));
				page.fill("//input[@name='street[0]']", data.get("Street"));
				page.fill("//input[@name='city']", data.get("City"));
				page.selectOption("//select[@name='region']", data.get("State"));
				page.fill("//input[@name='postcode']", data.get("ZipCode"));
				page.fill("//input[@name='telephone']", data.get("Phone"));
				page.click("//button[contains(@class,'checkout-address-form__buttons-save btn btn')]");
				waitUtils.waitUntilPageIsReady(page);
				System.out.println("✅ Shipping address filled.");

			} else {

				System.out.println("New Address Button is not Enabled.........");
				waitUtils.waitUntilElementEnabled(page, "//div[@x-ref='freegift']");
				closeGWPPopup();
				waitUtils.waitUntilElementEnabled(page, "//section[@id='quote-summary']");
				waitUtils.waitUntilElementEnabled(page, "//input[@name='email']");
				page.fill("//input[@name='email_address']", data.get("ShippingEmail"));
				page.fill("//input[@name='firstname']", data.get("FirstName"));
				page.fill("//input[@name='lastname']", data.get("LastName"));
				page.fill("//input[@name='street[0]']", data.get("Street"));
				page.fill("//input[@name='city']", data.get("City"));
				page.selectOption("//select[@name='region']", data.get("State"));
				page.fill("//input[@name='postcode']", data.get("ZipCode"));
				page.fill("//input[@name='telephone']", data.get("Phone"));
				System.out.println("✅ Shipping address filled.");
			}

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
			waitUtils.waitUntilElementEnabled(page, "//iframe[@title='Secure payment input frame']");
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