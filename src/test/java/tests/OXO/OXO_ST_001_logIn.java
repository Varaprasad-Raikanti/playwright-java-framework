package tests.OXO;

import utils.ConfigReader;
import utils.PlaywrightManager;
import utils.TestSetup;

import com.microsoft.playwright.Page;
import io.qameta.allure.*;

import org.testng.SkipException;
import org.testng.annotations.*;

import pages.OXOHelper;
import utils.ExcelUtils;

import java.util.Map;
import java.util.Iterator;
import java.util.List;

@Epic("Demo QA Form Automation")
@Feature("User Registration Form")
@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class OXO_ST_001_logIn {

    private Page page;

    @Step("Load config and initialize Playwright")
    @BeforeClass
    public void setupConfig() {
        TestSetup.initializePlaywrightWithConfig("OXO");
    }

    @Test(dataProvider = "Login", description = "Verify Login Functionality with valid data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User fills out the Login form and submits successfully")
    @Description("Test fills in user details and submits the login form")
    public void OXO_Login(Map<String, String> testData) {
        OXOHelper OXO = new OXOHelper(page);
        OXO.Login();  // Empty
    }

    @DataProvider(name = "registrationData")
    public Iterator<Object[]> getRegistrationData() {
        List<Map<String, String>> allData = ExcelUtils.readAllData("test-data/testdata.xlsx", "Sheet1");
        return allData.stream().map(data -> new Object[]{data}).iterator();
    }

    @Step("Tear down Playwright instance")
    @AfterMethod
    public void tearDown() {
        PlaywrightManager.tearDown();
    }
}
