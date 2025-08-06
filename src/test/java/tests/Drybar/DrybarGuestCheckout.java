package tests.Drybar;

import java.util.Iterator;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.microsoft.playwright.Page;

import base.TestBase;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import pages.Drybar.DrybarHelper;
import utils.ExcelUtils;
import utils.PlaywrightManager;
import utils.TestSetup;

@Epic("Drybar Guest Checkout with CC")
@Feature("Checkout Functionality with CC")
@Listeners({ utils.TestListener.class })
public class DrybarGuestCheckout extends TestBase {

	private Page page;

	@BeforeClass
	public void setupConfig() {
		TestSetup.initializePlaywrightWithConfig("Drybar");
	}

	@BeforeMethod
	public void setup() {
		page = TestSetup.initializePageAndNavigate();
	}

	@Test(description = "Order place with Guest User CC Payment ")
	public void DrybarGuest_Checkout() {
		try {
			Map<String, Map<String, String>> allSections = ExcelUtils.readTestData("Drybar", "Sheet1");
			DrybarHelper db = new DrybarHelper(page, allSections);
			db.acceptCookies();
			db.searchProduct("ProductName");
			db.addToCart();
			db.fillShippingAddress("ShippingAddress");
			db.fillPaymentDetails("PaymentDetails");

		} catch (Exception e) {
			Assert.fail("Test failed: " + e.getMessage());
		}
	}

	@DataProvider(name = "Login")
	public Iterator<Object[]> getLoginData() {
		return ExcelUtils.getExcelData("Drybar", "Login", "ProductName", "ShippingAddress", "PaymentDetails");
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		PlaywrightManager.tearDown();
	}
}
