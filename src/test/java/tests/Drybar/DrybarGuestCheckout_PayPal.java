package tests.Drybar;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.microsoft.playwright.Page;

import base.TestBase;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import pages.Drybar.DrybarHelper;
import utils.ExcelUtils;
import utils.PlaywrightManager;
import utils.TestSetup;

@Epic("Drybar Guest Checkout with PayPal")
@Feature("Checkout Functionality with PayPal Payment")
@Listeners(utils.TestListener.class)
public class DrybarGuestCheckout_PayPal extends TestBase {

	private Page page;
	private DrybarHelper drybar;

	@BeforeMethod
	public void setup() {
		TestSetup.initializePlaywrightWithConfig("Drybar");
		page = TestSetup.initializePageAndNavigate();
		Map<String, Map<String, String>> data = ExcelUtils.readTestData("Drybar", "Sheet1");
		drybar = new DrybarHelper(page, data);
	}

	@Test(description = "Order place with Guest User Paypal Payment")
	@Severity(SeverityLevel.MINOR)
	public void DrybarGuestCheckout_PayPalPayment() {
		try {
			drybar.acceptCookies();
			drybar.searchProduct("ProductName");
			drybar.addToCart();
			drybar.fillPayPalPaymentDetails("PayPalPaymentDetails");
			drybar.placeOrder();

		} catch (Exception e) {
			Assert.fail("Test failed: " + e.getMessage());
		}
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		PlaywrightManager.tearDown();
	}
}
