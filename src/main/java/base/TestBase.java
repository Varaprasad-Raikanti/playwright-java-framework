package base;

import com.microsoft.playwright.*;
import utils.ConfigReader;

import java.awt.*;

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

        System.out.println("Browser: " + browserName);
        System.out.println("Headless: " + isHeadless);
        System.out.println("Base URL: " + baseUrl);

        if (browserName == null || browserName.trim().isEmpty()) {
            throw new RuntimeException("Property 'browser' is missing in config.properties");
        }

        // Get screen resolution for full screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = (int) screenSize.getWidth();
        int screenHeight = (int) screenSize.getHeight();

        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
            .setHeadless(isHeadless);

        // Add --start-maximized only for Chromium or Chrome
        if (browserName.equalsIgnoreCase("chromium") || browserName.equalsIgnoreCase("chrome")) {
            launchOptions.setArgs(java.util.Arrays.asList("--start-maximized"));
        }

        switch (browserName.toLowerCase()) {
            case "chromium":
                browser = playwright.chromium().launch(launchOptions);
                break;

            case "firefox":
                browser = playwright.firefox().launch(launchOptions);
                break;

            case "webkit":
                browser = playwright.webkit().launch(launchOptions);
                break;

            case "chrome":
                launchOptions.setChannel("chrome"); // Use actual Chrome browser
                browser = playwright.chromium().launch(launchOptions);
                break;

            default:
                throw new IllegalArgumentException("Unsupported browser: " + browserName);
        }

        // Set viewport size to full screen (simulate maximize)
        context = browser.newContext(new Browser.NewContextOptions()
            .setViewportSize(screenWidth, screenHeight));

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
