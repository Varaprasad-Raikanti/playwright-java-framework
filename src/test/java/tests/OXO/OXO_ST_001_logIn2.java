package tests.OXO;

import utils.ConfigReader;
import utils.PlaywrightManager;
import utils.TestSetup;
import com.microsoft.playwright.Page;

import base.TestBase;
import io.qameta.allure.*;
import org.testng.SkipException;
import org.testng.annotations.*;
import pages.OXOHelper;
import utils.ExcelUtils;
import java.util.Map;
import java.util.Iterator;
import java.util.List;

@Epic("OXO Login Test case")
@Feature("User Login Form")
//@Listeners({io.qameta.allure.testng.AllureTestNg.class})
@Listeners({utils.TestListener.class})
public class OXO_ST_001_logIn2 extends TestBase {
    private Page page;
    @Step("Load config and initialize Playwright")
    @BeforeClass
    public void setupConfig() {
        TestSetup.initializePlaywrightWithConfig("OXO");
    }
    
    @BeforeMethod
    public void setup() {
        this.page = TestSetup.initializePageAndNavigate();
    }

    @Test(dataProvider = "Login", description = "Verify Login form submission with valid data")
    @Severity(SeverityLevel.CRITICAL)
    @Story("User fills out the Login form and submits successfully")
    @Description("Test fills in user details from Excel and submits the Login form")
    public void loginForm(Map<String, String> testData) {
        
        OXOHelper OXO = new OXOHelper(page);
        OXO.decline_Cookies();
    }

    @DataProvider(name = "Login")
    public Iterator<Object[]> getRegistrationData() {
        return ExcelUtils.getExcelData("test-data/testdata.xlsx", "Sheet1");
    }


    @Step("Tear down Playwright instance")
    @AfterMethod
    public void tearDown() {
        PlaywrightManager.tearDown();
    }
}
