package pages.Drybar;

import java.util.Map;

import org.testng.Assert;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.FrameLocator;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

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
			ScreenshotUtils.attachScreenshot(page, "Failure", "acceptCookies");
			Assert.fail("❌ Failed to accept cookies:");
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
				Close_Popup();
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
				Close_Popup();
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
			ScreenshotUtils.attachScreenshot(page, "Failure", "addToCart/Checkout");
			Assert.fail("❌ Add to cart or Checkout failed");
		}
	}

	public void shoppingCartValidation() {
		try {
			// Back to cart
			waitUtils.waitUntilElementEnabled(page, "//span[text()='Back To Cart']");
			Locator backToCart = page.locator("//span[text()='Back To Cart']");
			Assert.assertTrue(backToCart.isEnabled(), "❌ 'Back To Cart' button is not enabled");
			backToCart.click();

			// Page validations
			waitUtils.waitUntilPageIsReady(page);
			waitUtils.waitUntilElementVisible(page, "//a[@title='Checkout']");
			waitUtils.validateCurrentUrl(page, ".drybar.com/checkout/cart/index/");
			waitUtils.validatePageTitle(page, "Shopping Cart");

			// Checkout button
			Locator checkoutBtn = page.locator("//a[@title='Checkout']");
			waitUtils.waitUntilElementEnabled(page, "//a[@title='Checkout']");
			Assert.assertTrue(checkoutBtn.isEnabled(), "❌ 'Checkout' button is not enabled");

			// Discount form
			Locator discountToggle = page.locator("#discount-form-toggle");
			waitUtils.waitUntilElementEnabled(page, "#discount-form-toggle");
			Assert.assertTrue(discountToggle.isEnabled(), "❌ 'Discount form toggle' is not enabled");

			// Cart items
			Locator cartItems = page.locator("//div[contains(@class,'checkout-cart-wrapper__item')]");
			waitUtils.waitUntilElementVisible(page, "//div[contains(@class,'checkout-cart-wrapper__item')]");
			Assert.assertTrue(cartItems.count() > 0, "❌ No cart items found");

			// Cart totals
			Locator cartTotals = page.locator("//div[@id='cart-totals']");
			waitUtils.waitUntilElementEnabled(page, "//div[@id='cart-totals']");
			Assert.assertTrue(cartTotals.isEnabled(), "❌ 'Cart totals' section is not enabled");

			System.out.println("✅ Shopping Cart page validation passed");
			ScreenshotUtils.attachScreenshot(page, "Success", "Shopping Cart Validation is Success");

		} catch (Exception e) {
			ScreenshotUtils.attachScreenshot(page, "Failure", "shoppingCartValidation");
			Assert.fail("❌ Shopping Cart validation failed: " + e.getMessage());
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
			ScreenshotUtils.attachScreenshot(page, "Failure", "login");
			Assert.fail("❌ Login failed");
		}
	}

	public void createAccount(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);

			waitUtils.waitUntilElementEnabled(page, "//button[@id='customer-menu']");
			page.click("//button[@id='customer-menu']");
			waitUtils.waitUntilElementEnabled(page, "//a[@title='Create an Account']");
			page.click("//a[@title='Create an Account']");
			waitUtils.waitUntilElementEnabled(page, "//span[text()='Sign Up']");

			page.fill("#firstname", data.get("FirstName"));
			page.fill("#lastname", data.get("LastName"));
			String uniqueEmail = data.get("NewEmail") + System.currentTimeMillis() + "@gmail.com";
			page.fill("#email_address", uniqueEmail);
			page.fill("#password", data.get("NewPassword"));
			page.fill("#password-confirmation", data.get("confirmNewPassword"));
			page.click("//span[text()='Sign Up']");
			waitUtils.waitUntilPageIsReady(page);
			waitUtils.validateCurrentUrl(page, ".drybar.com/customer/account/");
			waitUtils.validatePageTitle(page, "Dashboard");
			ScreenshotUtils.attachScreenshot(page, "Success", "Account Created Successfully");

		} catch (Exception | AssertionError e) {
			System.out.println("❌ Create Account failed: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "Failure", "CreateAccount");
			Assert.fail("❌ CreateAccount failed");
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
			ScreenshotUtils.attachScreenshot(page, "Failure", "searchProduct");
			Assert.fail("❌ Product Serach is failed");
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
			ScreenshotUtils.attachScreenshot(page, "Failure", "Category Select");
			Assert.fail("❌ Category Selection is failed");
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
			closeGWPPopup();
		} catch (Exception e) {
			System.out.println("❌ Failed to fill shipping address: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "Failure", "fillShippingAddress");
			Assert.fail("❌ Failed to fill shipping address");
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
				waitUtils.waitUntilElementEnabled(page, "//input[@name='lastname']");
				page.fill("//input[@name='firstname']", data.get("FirstName"));
				page.fill("//input[@name='lastname']", data.get("LastName"));
				page.fill("//input[@name='street[0]']", data.get("Street"));
				page.fill("//input[@name='city']", data.get("City"));
				page.selectOption("//select[@name='region']", data.get("State"));
				page.fill("//input[@name='postcode']", data.get("ZipCode"));
				waitUtils.waitUntilElementEnabled(page, "//input[@name='telephone']");
				Thread.sleep(2000);
				page.fill("//input[@name='telephone']", data.get("Phone"));
				waitUtils.waitUntilElementEnabled(page,
						"//button[contains(@class,'checkout-address-form__buttons-save btn btn')]");
				page.click("//button[contains(@class,'checkout-address-form__buttons-save btn btn')]");
				waitUtils.waitUntilPageIsReady(page);
				System.out.println("✅ Shipping address filled.");

			} else {

				System.out.println("New Address Button is not Enabled.........");
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
				closeGWPPopup();
			}

		} catch (Exception e) {
			System.out.println("❌ Failed to fill shipping address: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "Failure", "fillShippingAddress");
			Assert.fail("❌ Failed to fill shipping address");
		}
	}

	public void fillPaymentDetails(String sectionName) {
		{
			try {
				Map<String, String> data = testDataSections.get(sectionName);

				waitUtils.waitUntilPageIsReady(page);

				// ✅ Ensure new card radio is selected
				Locator newCardRadio = page.locator("(//input[@name='use_saved_stripe_method'])[2]");
				if (newCardRadio.count() > 0) {
					if (!newCardRadio.isChecked()) {
						System.out.println("Selecting New Card option...");
						newCardRadio.check(); // Works even if input is hidden
					} else {
						System.out.println("New Card option already selected");
					}
				} else {
					System.out.println("New Card not found");
					FrameLocator frame = page.frameLocator("//iframe[@title='Secure payment input frame']");
					frame.locator("#Field-numberInput").fill(data.get("CardNumber"));
					frame.locator("#Field-expiryInput").fill(data.get("ExpDate"));
					frame.locator("//input[@aria-describedby='Field-cvcError cvcDesc']").fill(data.get("CVV"));

				}

				// ✅ Fill payment iframe with new card details
				FrameLocator frame = page.frameLocator("//iframe[@title='Secure payment input frame']");
				frame.locator("#Field-numberInput").fill(data.get("CardNumber"));
				frame.locator("#Field-expiryInput").fill(data.get("ExpDate"));
				frame.locator("//input[@aria-describedby='Field-cvcError cvcDesc']").fill(data.get("CVV"));

			} catch (Exception e) {
				ScreenshotUtils.attachScreenshot(page, "Failure", "FAILED to fill Credit Card Payment Details");
				Assert.fail("❌ Failed to fill Credit Card payment details: " + e.getMessage());
			}

		}
	}

	public void fillPayPalPaymentDetails(String sectionName) {
		try {
			Map<String, String> data = testDataSections.get(sectionName);

			waitUtils.waitUntilPageIsReady(page);
			waitUtils.waitUntilElementEnabled(page, "//div[@x-ref='freegift']");
			closeGWPPopup();

			// Wait for PayPal iframe
			waitUtils.waitUntilElementVisible(page, "//iframe[@title='PayPal']");
			FrameLocator frame = page.frameLocator("//iframe[@title='PayPal']");

			// Capture new PayPal window
			BrowserContext context = page.context();
			Page paypalPage = context.waitForPage(() -> {
				frame.locator("//div[@aria-label='PayPal']//div[@aria-label='PayPal']").click();
			});

			// Wait until PayPal login page is loaded
			paypalPage.waitForLoadState(LoadState.DOMCONTENTLOADED);

			// Fill PayPal email
			waitUtils.waitUntilElementVisible(paypalPage, "//input[@placeholder='Email or mobile number']");
			paypalPage.fill("//input[@placeholder='Email or mobile number']", data.get("PayPalEmailAddress"));
			paypalPage.locator("//button[text()='Next']").click();

			// Fill password
			waitUtils.waitUntilElementVisible(paypalPage, "//input[@name='login_password']");
			paypalPage.fill("//input[@name='login_password']", data.get("PayPalPassword"));
			paypalPage.locator("//button[text()='Log In']").click();

			// Complete purchase in PayPal
			waitUtils.waitUntilElementVisible(paypalPage, "//button[text()='Complete Purchase']");
			paypalPage.click("//button[text()='Complete Purchase']");

			// ✅ Wait for checkout page to load after redirect
			page.waitForLoadState(LoadState.DOMCONTENTLOADED);

			// ✅ Wait for shipping details to be populated (look for another stable field
			// like firstname)
			Thread.sleep(3000);
			waitUtils.waitUntilElementVisible(page, "//input[@name='firstname']");

			// ✅ Ensure telephone field is visible & ready
			waitUtils.waitUntilElementEnabled(page, "//input[@name='telephone']");

			// ✅ Check if telephone field is empty before filling
			String phoneValue = page.locator("//input[@name='telephone']").inputValue();
			if (phoneValue == null || phoneValue.trim().isEmpty()) {
				page.fill("//input[@name='telephone']", data.get("Phone"));
				System.out.println("📞 Phone field filled with: " + data.get("Phone"));
			} else {
				System.out.println("📞 Phone field already has value: " + phoneValue);
			}

		} catch (Exception e) {
			ScreenshotUtils.attachScreenshot(page, "Failure", "Failed to fill Paypal payment & Auto Shipping details");
			Assert.fail("❌ Failed to fill Paypal payment & Shipping details: " + e.getMessage());
		}
	}

	public void placeOrder() {
		System.out.println("Entered to Place Order.");
		final String placeOrderBtn = "//div[contains(@class,'place-order')]//button[contains(text(),'Place Order')]";
		final int MAX_WAIT_MS = 120000;

		final String[] SUCCESS_SELECTORS = { "//a[contains(@class,'order-number') and contains(@class,'link')]",
				"//h1[contains(.,'Thank you') or contains(.,'Thank You')]",
				"//*[contains(@class,'order-success') or contains(@class,'checkout-success')]",
				"//*[contains(.,'Order number') or contains(.,'Order #')]" };

		try {
			closeGWPPopup();
			waitUtils.waitUntilElementVisible(page, placeOrderBtn);

			Locator btn = page.locator(placeOrderBtn);
			btn.waitFor();

			// Wait until button becomes enabled
			while (!btn.isEnabled()) {
				Thread.sleep(200);
			}

			// Ensure button HTML is stable for 1 second
			String prevHtml = btn.innerHTML();
			long stableSince = System.currentTimeMillis();
			while (System.currentTimeMillis() - stableSince < 1000) {
				String currentHtml = btn.innerHTML();
				if (!currentHtml.equals(prevHtml)) {
					prevHtml = currentHtml;
					stableSince = System.currentTimeMillis();
				}
				Thread.sleep(200);
			}

			// Start timing
			long start = System.currentTimeMillis();
			boolean successDetected = false;

			// Click and handle popup if needed
			btn.click();
			closeGWPPopup();

			// Wait for success confirmation
			long deadline = System.currentTimeMillis() + MAX_WAIT_MS;
			while (System.currentTimeMillis() < deadline) {
				String url = page.url();
				if (url.contains("/checkout/onepage/success/") || url.contains("/order-confirmation")) {
					successDetected = true;
					break;
				}

				for (String sel : SUCCESS_SELECTORS) {
					if (page.locator(sel).first().isVisible(new Locator.IsVisibleOptions().setTimeout(500))) {
						successDetected = true;
						break;
					}
				}

				if (successDetected)
					break;
				Thread.sleep(500);
			}

			waitUtils.waitUntilPageIsReady(page);

			// ✅ Validation
			if (successDetected) {
				ScreenshotUtils.attachScreenshot(page, "Success", "Order Placed Successfully");
				long tookMs = System.currentTimeMillis() - start;
				System.out.println("✅ Order placement confirmed in " + tookMs + " ms.");
				Assert.assertTrue(true, "Order was placed successfully."); 
			} else {
				ScreenshotUtils.attachScreenshot(page, "Failure", "Order Confirmation Not Detected");
				Assert.fail("❌ Order placement failed: success page or confirmation element not detected.");
			}

		} catch (Exception e) {
			System.out.println("❌ placeOrder error: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "Failure", "placeOrder Exception");
			Assert.fail("❌ Failed in placeOrder: " + e.getMessage());
		}
	}

	public boolean isLoginSuccessful() {
		try {
			return page.locator("//a[@title='My Account']").isVisible();
		} catch (Exception e) {
			System.out.println("❌ Failed to verify login: " + e.getMessage());
			ScreenshotUtils.attachScreenshot(page, "Failure", "isLoginSuccessful");
			Assert.fail("❌ Failed to verify Login");
			return false;
		}
	}
}
