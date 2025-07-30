package base;

import com.microsoft.playwright.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ConfigReader;

public class BaseTest {
    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    @BeforeMethod
    public void setUp() {
        ConfigReader.loadConfig();

        playwright = Playwright.create();
        BrowserType browserType;

        String browserName = ConfigReader.get("browser").toLowerCase();

        switch (browserName) {
            case "firefox":
                browserType = playwright.firefox();
                break;
            case "webkit":
                browserType = playwright.webkit();
                break;
            case "chromium":
            default:
                browserType = playwright.chromium();
                break;
        }

        browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();
        page = context.newPage();

        page.navigate(ConfigReader.get("url"));
    }

    @AfterMethod
    public void tearDown() {
        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
