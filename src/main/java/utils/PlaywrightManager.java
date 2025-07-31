package utils;

import com.microsoft.playwright.*;

public class PlaywrightManager {

    private static Playwright playwright;
    private static Browser browser;
    private static Page page;

    public static void initialize() {
        String browserName = ConfigReader.getProperty("browser").toLowerCase();
        boolean isHeadless = Boolean.parseBoolean(ConfigReader.getProperty("headless"));
        String url = ConfigReader.getProperty("url");

        playwright = Playwright.create();
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(isHeadless);

        browser = switch (browserName) {
            case "chromium" -> playwright.chromium().launch(options);
            case "firefox" -> playwright.firefox().launch(options);
            case "webkit" -> playwright.webkit().launch(options);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browserName);
        };

        page = browser.newPage();
        page.navigate(url);
    }

    public static Page getPage() {
        return page;
    }

    public static void tearDown() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
