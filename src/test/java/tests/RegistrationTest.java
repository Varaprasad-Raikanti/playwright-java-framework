package tests;

import java.util.Iterator;
import java.util.Map;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.microsoft.playwright.Page;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import utils.ConfigReader;
import utils.ExcelUtils;
import utils.PlaywrightManager;
import utils.TestSetup;

@Epic("Demo QA Form Automation")
@Feature("User Registration Form")
@Listeners({ io.qameta.allure.testng.AllureTestNg.class })
public class RegistrationTest {

	private Page page;

	@Step("Load config and initialize Playwright")
	@BeforeClass
	public void setupConfig() {
		TestSetup.initializePlaywrightWithConfig("Demo");
	}

	@BeforeMethod
	public void setup() {
		this.page = PlaywrightManager.getPage();
		if (page == null) {
			throw new SkipException("Playwright page is null — skipping test.");
		}

		// ✅ Load URL from config
		String appUrl = ConfigReader.getProperty("url");
		if (appUrl == null || appUrl.isEmpty()) {
			throw new RuntimeException("URL not found in config file");
		}

		// ✅ Navigate to the application
		page.navigate(appUrl);
		page.waitForLoadState(); // Wait for full page load
	}

	@Test(dataProvider = "registrationData", description = "Verify registration form submission with valid data")
	@Severity(SeverityLevel.CRITICAL)
	@Story("User fills out the registration form and submits successfully")
	@Description("Test fills in user details from Excel and submits the registration form")
	public void testRegistrationForm(Map<String, String> testData) {
		System.out.println("Running test with data: " + testData);

		/*
		 * OXOHelper regPage = new OXOHelper(page);
		 * regPage.enterFirstName(testData.get("FirstName"));
		 * regPage.enterLastName(testData.get("LastName"));
		 * regPage.enterEmail(testData.get("Email")); regPage.selectGender(); //
		 * Optional: Make dynamic regPage.enterMobile(testData.get("Mobile"));
		 * regPage.clickSubmit();
		 */
	}

	@DataProvider(name = "Login")
	public Iterator<Object[]> getLoginData() {
		return ExcelUtils.getExcelData("Drybar", "Login", "ProductName", "ShippingAddress", "PaymentDetails");
	}

	@Step("Tear down Playwright instance")
	@AfterMethod
	public void tearDown() {
		PlaywrightManager.tearDown();
	}
}
