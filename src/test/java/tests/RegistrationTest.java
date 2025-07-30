package tests;

import com.microsoft.playwright.*;
import io.qameta.allure.*;
import pages.RegistrationPage;

import org.testng.annotations.*;
import utils.ConfigReader;
import utils.ExcelUtils;

import java.util.Map;

@Epic("Demo QA Form Automation")
@Feature("User Registration Form")
@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class RegistrationTest {
    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeClass
    @Step("Set up Playwright browser and navigate to the registration page")
    public void setUp() {
        String url = ConfigReader.getProperty("url");
        boolean isHeadless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));

        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(isHeadless)
        );
        page = browser.newPage();
        page.navigate(url);
    }

    @Test(description = "Verify registration form submission with valid data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User fills out the registration form and submits successfully")
    @Description("Test fills in user details from Excel and submits the registration form")
    public void testRegistrationForm() {
        String excelPath = "test-data/testdata.xlsx";
        String sheetName = "Sheet1";
        int rowNumber = 1;

        Map<String, String> testData = ExcelUtils.getTestData(excelPath, sheetName, rowNumber);

        RegistrationPage regPage = new RegistrationPage(page);
        regPage.enterFirstName(testData.get("FirstName"));
        regPage.enterLastName(testData.get("LastName"));
        regPage.enterEmail(testData.get("Email"));
        regPage.selectGender(); // Default/hardcoded gender
        regPage.enterMobile(testData.get("Mobile"));
        regPage.clickSubmit();
    }

    @AfterClass
    @Step("Tear down Playwright browser instance")
    public void tearDown() {
        browser.close();
        playwright.close();
    }
}