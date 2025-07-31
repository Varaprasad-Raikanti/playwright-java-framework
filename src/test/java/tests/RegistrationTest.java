package tests;

import utils.PlaywrightManager;
import com.microsoft.playwright.Page;
import io.qameta.allure.*;
import org.testng.annotations.*;
import pages.RegistrationPage;
import utils.ExcelUtils;

import java.util.Map;

@Epic("Demo QA Form Automation")
@Feature("User Registration Form")
@Listeners({io.qameta.allure.testng.AllureTestNg.class})
public class RegistrationTest {

    private Page page;

    @BeforeClass
    public void setUp() {
        PlaywrightManager.initialize();
        page = PlaywrightManager.getPage();
    }

    @Test(description = "Verify registration form submission with valid data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User fills out the registration form and submits successfully")
    @Description("Test fills in user details from Excel and submits the registration form")
    public void testRegistrationForm() {
        Map<String, String> testData = ExcelUtils.getTestData("test-data/testdata.xlsx", "Sheet1", 1);

        RegistrationPage regPage = new RegistrationPage(page);
        regPage.enterFirstName(testData.get("FirstName"));
        regPage.enterLastName(testData.get("LastName"));
        regPage.enterEmail(testData.get("Email"));
        regPage.selectGender(); // Default/hardcoded gender
        regPage.enterMobile(testData.get("Mobile"));
        regPage.clickSubmit();
    }

    @AfterClass
    public void tearDown() {
        PlaywrightManager.tearDown();
    }
}
