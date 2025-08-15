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

@Epic("Drybar Account Creation")
@Feature("Create New User For Drybar")
@Listeners(utils.TestListener.class)
public class DrybarCreateAccount extends TestBase {

	private Page page;
	private DrybarHelper drybar;

	@BeforeMethod
	public void setup() {
		TestSetup.initializePlaywrightWithConfig("Drybar");
		page = TestSetup.initializePageAndNavigate();
		Map<String, Map<String, String>> data = ExcelUtils.readTestData("Drybar", "Sheet1");
		drybar = new DrybarHelper(page, data);
	}

	@Test(description = "Create an Account for Drybar")
	@Severity(SeverityLevel.BLOCKER)
	public void Drybar_CreateAccount() {
		try {
			drybar.acceptCookies();
			drybar.createAccount("AccountDetails");
		} catch (Exception e) {
			Assert.fail("Test failed: " + e.getMessage());
		}
	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		PlaywrightManager.tearDown();
	}
}
