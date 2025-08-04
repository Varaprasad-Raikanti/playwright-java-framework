package tests;

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

        OXOHelper regPage = new OXOHelper(page);
        regPage.enterFirstName(testData.get("FirstName"));
        regPage.enterLastName(testData.get("LastName"));
        regPage.enterEmail(testData.get("Email"));
        regPage.selectGender(); // Optional: Make dynamic
        regPage.enterMobile(testData.get("Mobile"));
        regPage.clickSubmit();
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
