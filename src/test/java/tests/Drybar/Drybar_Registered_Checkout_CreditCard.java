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

@Epic("Drybar Register Checkout with CC")
@Feature("Checkout Functionality with CC")
@Listeners(utils.TestListener.class)
public class Drybar_Registered_Checkout_CreditCard extends TestBase {

	private Page page;
	private DrybarHelper drybar;

	@BeforeMethod
	public void setup() {
		TestSetup.initializePlaywrightWithConfig("Drybar");
		page = TestSetup.initializePageAndNavigate();
		Map<String, Map<String, String>> data = ExcelUtils.readTestData("Drybar", "Sheet1");
		drybar = new DrybarHelper(page, data);
	}

	@Test(description = "Order placement with Register User CC Payment")
	@Severity(SeverityLevel.CRITICAL)
	public void Drybar_Registered_CheckoutCreditCard() {
		try {
			drybar.acceptCookies();
			drybar.login("Login");
			drybar.addToCart();
			drybar.fillRegShippingAddress("ShippingAddress");
			drybar.fillPaymentDetails("PaymentDetails");
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
