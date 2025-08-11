package tests.OXO;

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
import pages.OXO.OXOHelper;
import utils.ExcelUtils;
import utils.PlaywrightManager;
import utils.TestSetup;

@Epic("OXO User Login")
@Feature("Login Functionality")
@Listeners(utils.TestListener.class)
public class OXO_ST_001_logIn2 extends TestBase {

	private Page page;
	private OXOHelper OXO;
	private Map<String, Map<String, String>> testData;

	@BeforeMethod
	public void setup() {
		TestSetup.initializePlaywrightWithConfig("OXO");
		page = TestSetup.initializePageAndNavigate();
		testData = ExcelUtils.readTestData("OXO", "Sheet1"); // Adjust sheet name as needed
		OXO = new OXOHelper(page, testData);
	}

	@Test(description = "Verify Login form submission with valid data")
	public void loginForm() {
		try {
			OXO.aceept_Cookies();

		} catch (Exception e) {
			Assert.fail("Login test failed: " + e.getMessage());
		}
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		PlaywrightManager.tearDown();
	}
}
