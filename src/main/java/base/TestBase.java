package base;

import com.microsoft.playwright.*;
import utils.ConfigReader;

public class TestBase {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    public void setup() {
        playwright = Playwright.create();

        String browserName = ConfigReader.getProperty("browser");
        boolean isHeadless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));
        String baseUrl = ConfigReader.getProperty("base.url");

        // Debug log
        System.out.println("Browser: " + browserName);
        System.out.println("Headless: " + isHeadless);
        System.out.println("Base URL: " + baseUrl);

        if (browserName == null || browserName.trim().isEmpty()) {
            throw new RuntimeException("Property 'browser' is missing in config.properties");
        }

        switch (browserName.toLowerCase()) {
            case "chromium":
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless));
                break;
            case "firefox":
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless));
                break;
            case "webkit":
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(isHeadless));
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        context = browser.newContext();
        page = context.newPage();

        if (baseUrl != null && !baseUrl.trim().isEmpty()) {
            page.navigate(baseUrl);
        } else {
            throw new RuntimeException("Property 'base.url' is missing in config.properties");
        }
    }

    public void tearDown() {
        if (playwright != null) {
            playwright.close();
        }
    }
}
